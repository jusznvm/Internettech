package client;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private int serverPort;

    public Client(int port){
        this.serverPort = port;
    }

    public void start() {
        try {
            this.socket = new Socket(InetAddress.getLocalHost(), serverPort);

            System.out.println("Please fill in your username: ");
            Scanner scanner = new Scanner(System.in);
            String userName = scanner.nextLine();
            ClientMessage helo = new ClientMessage(MessageType.HELO, userName);

            //THREAD for reading input
            InputStream is = socket.getInputStream();
            InputReader ir = new InputReader(is);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class InputReader implements Runnable {
        private BufferedReader reader;
        private boolean isRunning;

        public InputReader(InputStream is) {
            this.reader = new BufferedReader(new InputStreamReader(is));
            this.isRunning = true;
        }

        @Override
        public void run() {

            do {
                try {
                    String line = reader.readLine();
                    line = line.contains(" ") ? line.split(" ")[0]: line;

                    ClientMessage clientMessage;

                    switch (line) {
                        case "HELO":
                            break;
                        case "BCST":
                            break;
                        case "PING":
                            break;
                        case "USERS":
                            break;
                        case "DM":
                            break;
                        case "MKGROUP":
                            break;
                        case "GETGROUPS":
                            break;
                        case "JOIN":
                            break;
                        case "SHOUT":
                            break;
                        case "LEAVE":
                            break;
                        case "KICK":
                            break;
                        case "QUIT":
                            // STOP HERE
                            break;
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while (this.isRunning);
        }
    }
}


// do lees socket, while er berichten zijn
// niet in 1 thread, keyboard input lezen & network input. Dus 2 verschillende threads ervoor
// client heeft dus 2 threads.

// keyboard:
// bufferedreader = new inputstreamreader(inputStream in)