package client;

import message.Message;
import message.MessageType;
import server.Server;
import utils.RSA;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Thread {
    private Socket socket;
    private Socket transferSocket;

    private final static int serverPort = 1337;
    private final static int transferPort = 4444;

    private PublicKey publicKey;
    private PrivateKey privateKey;
    private String keyFileName;

    private String username;

    public LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        new Client().start();
    }

    public Client() {
        try {
            KeyPair keyPair = RSA.buildKeyPair();
            this.publicKey = keyPair.getPublic();
            this.privateKey = keyPair.getPrivate();

            this.keyFileName = new SimpleDateFormat("HHmmss").format(new Date());
            RSA.generateKeyFiles(privateKey, publicKey, keyFileName);
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        try {
            this.socket = new Socket(InetAddress.getLocalHost(), serverPort);

            System.out.println("Connected to chat server");

            new ReadThread().start();
            new WriteThread().start();

            while (messages.isEmpty()) {
            }

            if (messages.peek().getMessageType().equals(MessageType.HELO))
                System.out.println("Please enter your username :)))");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ReadThread extends Thread {
        private BufferedReader reader;
        private boolean isValidated = false;

        public ReadThread() {

            try {
                InputStream input = socket.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
            } catch (IOException ex) {
                System.out.println("Error getting input stream: " + ex.getMessage());
                ex.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                do {
                    isValidated = isClientValidated();
                } while (!isValidated);

                do {
                    String line = reader.readLine();
                    if (line.startsWith(MessageType.GETFILE.toString())) {
                        if (line.contains("accepted")) {
                            transferSocket = new Socket(InetAddress.getLocalHost(), transferPort);

                            PrintWriter transferHandlerWriter = new PrintWriter(transferSocket.getOutputStream());

                            transferHandlerWriter.println(username);
                            transferHandlerWriter.flush();

                            String file = line.split(" ")[2];
                            new SendFileHandler(file, transferSocket).run();
                        } else if (line.contains("started")) {
                            transferSocket = new Socket(InetAddress.getLocalHost(), transferPort);
                            PrintWriter transferHandlerWriter = new PrintWriter(transferSocket.getOutputStream());

                            transferHandlerWriter.println(username);
                            transferHandlerWriter.flush();

                            String fileType = line.split(" ")[2].split("\\.")[1];
                            new ReceiveFileHandler(fileType, transferSocket).run();
                        }
                    }

                    System.out.println(line);
                } while (isValidated);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private boolean isClientValidated() {
            try {
                String line = reader.readLine();
                System.out.println(line);

                if (line.contains((MessageType.HELO).toString()) || line.contains((MessageType.ERR).toString())) {
                    Message msg = new Message(MessageType.HELO.toString(), "");
                    messages.put(msg);
                } else if (line.contains((MessageType.OK).toString())) {
                    System.out.println("Welcome, you're successfully logged in! \n" +
                            "The following commands are available: \n" +
                            "USERS - returns a list of connected users \n" +
                            "DM [username] [message] - private message an user \n" +
                            "GETGROUPS - shows all groups \n" +
                            "MKGROUP [groupname] - create a group \n" +
                            "JOIN [groupname] - join a group \n" +
                            "SHOUT [groupname] [message] - send a message to all users in the group \n" +
                            "KICK [groupname] [user] - kick user from a group (group owner only!) \n" +
                            "LEAVE [groupname] - leave a group \n" +
                            "DMFILE [username] [filename.extension] - send a file, e.g. 'DMFILE user file.txt'");
                    return true;
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

    public class WriteThread extends Thread {
        private PrintWriter writer;

        public WriteThread() {
            try {
                this.writer = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            Scanner sc = new Scanner(System.in);

            while (true) {
                String line = sc.nextLine();
                Message msg = messages.peek();

                try {

                    if (msg != null && msg.getMessageType().equals(MessageType.HELO)) {
                        msg = messages.take();
                        msg.setPayload(line);
                        msg.setPublicKey(publicKey);

                        username = line;

                    } else if (containsMessageType(line)) {
                        String messageType = line.contains(" ") ? line.split(" ")[0] : line;
                        String payLoad = "";

                        if (line.split(" ").length >= 2) {
                            payLoad = line.split(" ", 2)[1];
                        }

                        msg = new Message(messageType, payLoad);

                    } else
                        msg = new Message("BCST", line);

                    writer.println(msg.toString());
                    writer.flush();

                } catch (InterruptedException  e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean containsMessageType(String line) {
            line = line.split(" ")[0];

            for (MessageType mt : MessageType.values()) {
                if (line.equalsIgnoreCase(mt.toString().toLowerCase()))
                    return true;
            }
            return false;
        }
    }
}
