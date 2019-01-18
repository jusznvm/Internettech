package client;

import sun.plugin2.message.Message;

import java.io.IOException;
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


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


// do lees socket, while er berichten zijn
// niet in 1 thread, keyboard input lezen & network input. Dus 2 verschillende threads ervoor
// client heeft dus 2 threads.

// keyboard:
// bufferedreader = new inputstreamreader(inputStream in)