package klient;

import client.ClientMessage;
import client.ServerMessage;
import utils.Utils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.Stack;

public class Client {

    private int port;

    private boolean isConnected = false;
    private Socket socket;

    MessageWriter messageWriter;
    MessageReader messageReader;

    private Stack<ClientMessage> clientMessages;
    private Stack<ServerMessage> serverMessages;

    public Client(int port) {
        this.port = port;

        this.clientMessages = new Stack<>();
        this.serverMessages = new Stack<>();
    }

    public void start() {
        try {
            // Socket for network traffic
            this.socket = new Socket(InetAddress.getLocalHost(), port);

            // InputStream and BufferedReader get all the input from server
            InputStream is = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            // Start reader thread so we can receive HELO message
            messageReader = new MessageReader(reader);
            new Thread(messageReader).start();

            // Initialize messageWriter
            OutputStream os = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(os);
            messageWriter = new MessageWriter(writer);
            new Thread(messageWriter).start();

            System.out.println("Please fill in a username: ");
            Scanner sc = new Scanner(System.in);
            String name = sc.nextLine();

            ClientMessage clientMessage = new ClientMessage(MessageType.HELO, name);
            while(serverMessages.empty())
            isConnected = Utils.validateServerMessage(clientMessage, serverMessages.pop());

            while(isConnected) {}

            System.out.println("Connection closed");

        } catch (IOException e) { e.printStackTrace(); }
    }

    /**
     * MessageReader class, reads in messages sent from server.
     */
    public class MessageReader implements Runnable{

        private boolean isRunning = false;
        private BufferedReader reader;

        public MessageReader(BufferedReader reader) {
            this.reader = reader;
        }

        @Override
        public void run() {
            while(isRunning) {
                try {
                    String line = reader.readLine();
                    ServerMessage msg = new ServerMessage(line);
                    messageHandler(msg);
                } catch (IOException e) { e.printStackTrace();}
            }
        }

        private void messageHandler(ServerMessage msg) {
            ClientMessage clientMessage = null;
            switch(msg.getMessageType()) {
                case HELO:
                    Scanner sc = new Scanner(System.in);
                    String name = sc.nextLine();
                    clientMessage = new ClientMessage(MessageType.HELO, name);
                    break;
                case OK:
                    System.out.println("L O O O L");
            }
            clientMessages.push(clientMessage);
        }
    }

    /**
     * MessageWriter class, writes messages to the server
     */
    public class MessageWriter implements Runnable{

        private boolean isRunning = false;
        private PrintWriter writer;

        public MessageWriter(PrintWriter writer) {
            this.writer = writer;
        }

        @Override
        public void run() {
            while(isRunning) {
                if(!clientMessages.empty()) {
                    String line = clientMessages.pop().toString();
                    writer.println(line);
                    writer.flush();
                }
            }
        }
    }
}
