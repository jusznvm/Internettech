package client;

import message.Message;
import message.MessageType;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;

public class Client extends Thread {
    private Socket socket;
    private int serverPort = 4444;

    public LinkedBlockingQueue<Message> messages = new LinkedBlockingQueue<>();

    public static void main(String[] args) {
        new Client().start();
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
                String welcomeLine = reader.readLine();

                if (welcomeLine.contains((MessageType.HELO).toString())) {
                    Message msg = new Message(MessageType.HELO.toString(), "");
                    messages.put(msg);
                }

                do {
                    String line = reader.readLine();
                    System.out.println(line);

//                    String[] msgString = line.split(" ", 2);
//
//                    Message msg = new Message(msgString[0], msgString[1]);
//                    msg.handleMessage();


                } while (reader.readLine() != null);

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
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
                    } else if (containsMessageType(line)) {
                        String messageType = line.contains(" ") ? line.split(" ")[0] : line;
                        String payLoad = line.split(" ", 2)[1];

                        msg = new Message(messageType, payLoad);
                    } else {
                        msg = new Message("BCST", line);
                    }

                    writer.println(msg.toString());
                    writer.flush();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean containsMessageType(String line) {
            line = line.split(" ")[0];

            for (MessageType mt : MessageType.values()) {
                if (line.contains(mt.toString().toLowerCase()))
                    return true;
            }
            return false;
        }
    }
}


// do lees socket, while er berichten zijn
// niet in 1 thread, keyboard input lezen & network input. Dus 2 verschillende threads ervoor
// client heeft dus 2 threads.

// keyboard:
// bufferedreader = new inputstreamreader(inputStream in)