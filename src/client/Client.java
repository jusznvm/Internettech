package client;

import klient.MessageType;
import utils.Utils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;

public class Client {
    private Socket socket;

    boolean isConnected = false;
    private int serverPort;

    MessageWriter messageWriter;
    MessageReader messageReader;

    private Stack<ClientMessage> clientMessages;
    private Stack<ServerMessage> serverMessages;

    public Client(int port) {
        this.serverPort = port;
        this.clientMessages = new Stack<ClientMessage>();
        this.serverMessages = new Stack<ServerMessage>();
    }

    public void start() {
        try {
            this.socket = new Socket(InetAddress.getLocalHost(), serverPort);

            InputStream is = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            messageReader = new MessageReader(reader);

            new Thread(messageReader).start();

            OutputStream os = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(os);
            messageWriter = new MessageWriter(writer);
            new Thread(messageWriter).start();

            while (serverMessages.empty()) {}

            ServerMessage serverMessage = serverMessages.pop();
            if (!serverMessage.getMessageType().equals(MessageType.HELO)) {
                System.out.println("Expecting a HELO message but received: " + serverMessage.toString());
            } else {
                System.out.println("Please fill in your username: ");
                Scanner sc = new Scanner(System.in);
                String name = sc.nextLine();

                ClientMessage heloMessage = new ClientMessage(MessageType.HELO, name);
                clientMessages.push(heloMessage);

                while (serverMessages.empty()) {}
                isConnected = Utils.validateServerMessage(heloMessage, serverMessages.pop());
                    while (isConnected) {
                        if (!serverMessages.empty()) {
                            ServerMessage received = (ServerMessage) serverMessages.pop();
                            if (received.getMessageType().equals(MessageType.BCST)) {
                                System.out.println(received.getPayload());
                            }
                        }
                    }
                }
                disconnect();
                System.out.println("Client disconnected!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        if (messageReader != null)
            messageReader.kill();
        if (messageWriter != null)
            messageWriter.kill();
        isConnected = false;
    }

    public class MessageReader implements Runnable {
        private BufferedReader reader;
        private boolean isRunning = true;

        public MessageReader(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {

            while(isRunning) {
                try {
                    String line = reader.readLine();
                    System.out.println(">> " + line);
                    ServerMessage message = new ServerMessage(line);
                    if(message.getMessageType().equals(MessageType.PING)) {
                        ClientMessage pongMessage = new ClientMessage(MessageType.PONG, "");
                        clientMessages.push(pongMessage);
                    }
                    if (message.getMessageType().equals(MessageType.DSCN)) {
                        System.out.println("Client disconnected by server.");
                        Client.this.disconnect();
                    }

                    serverMessages.push(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        public void kill() {
            isRunning = false;
        }
    }

    public class MessageWriter implements Runnable {

        private boolean isRunning = true;
        PrintWriter writer;


        public MessageWriter(final PrintWriter writer) {
            this.writer = writer;
        }

        @Override
        public void run() {
            while (isRunning) {

                BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
                String line = null;
                try {
                    line = bufReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ClientMessage clientMessage;

                if (line != null) {
                    if (line.equals("quit")) {
                        clientMessage = new ClientMessage(MessageType.QUIT, "");
                        isConnected = false;

                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        clientMessage = new ClientMessage(MessageType.BCST, line);
                    }
                    writeToServer(clientMessage, writer);

                    System.out.println("Type a broadcast message: ");
                }

            }
        }

        private void writeToServer(ClientMessage msg, final PrintWriter writer) {
            System.out.println("<< " + msg);
            writer.println(msg);
            writer.flush();
        }

        public void kill() {
            isRunning = false;
        }
    }
}


// do lees socket, while er berichten zijn
// niet in 1 thread, keyboard input lezen & network input. Dus 2 verschillende threads ervoor
// client heeft dus 2 threads.

// keyboard:
// bufferedreader = new inputstreamreader(inputStream in)