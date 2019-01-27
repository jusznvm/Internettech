package server;

import model.ClientInfo;
import model.TransferRequest;
import utils.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class TransferHandler extends Thread {
    private TransferRequest transferRequest;

    private Socket sender, receiver;

    public TransferHandler(TransferRequest transferRequest) {
        this.transferRequest = transferRequest;

    }

    /**
     * Start sending file bytes to the receiver
     */
    @Override
    public void run() {
        try {
            ServerSocket socket = new ServerSocket(Server.TRANSFER_PORT);

            while(true) {
                Socket client = socket.accept();

                if (client.isConnected()) {
                    System.out.println("A client connected to transfer thread");
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

                String name = reader.readLine();

                String connectedClient = Utils.findUserFromActiveClients(name).getUserName();

                if (connectedClient.equalsIgnoreCase(transferRequest.getSender().getUserName())) {
                    sender = client;
                    System.out.println("Sender connected to transfer thread");
                }

                if (connectedClient.equalsIgnoreCase(transferRequest.getReceiver().getUserName())) {
                    receiver = client;
                    System.out.println("Receiver connected to transfer thread");
                }

                if (sender != null && sender.isConnected()) {
                    if (receiver == null) {
                        break;
                    }

                    byte[] bytes = new byte[8000];
                    int bytesRead;

                    while ((bytesRead = client.getInputStream().read(bytes, 0, bytes.length)) > 0) {
                        receiver.getOutputStream().write(bytes, 0, bytesRead);
                    }

                    receiver.getOutputStream().flush();
                    receiver.getOutputStream().close();
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


