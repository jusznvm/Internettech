package server;

import client.ClientInfo;
import message.Message;
import message.MessageType;
import message.type.*;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientHandler extends Thread {

    private Socket client;
    private BlockingQueue<String> queue;

    private PrintWriter writer;

    private boolean isValidated = false;
    private ClientInfo clientInfo;

    public ClientHandler(Socket client, BlockingQueue<String> queue) {
        this.client = client;
        this.queue = queue;
    }

    @Override
    public void run() {

        try {
            writer = new PrintWriter(client.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            writer.println("HELO");
            writer.flush();

            while (true) {
                String line = reader.readLine();
                System.out.println("read input: " + line);

                String messageType = line.contains(" ") ? line.split(" ")[0] : line;
                String payLoad = line.split(" ", 2)[1];

                Message message = new Message(messageType, payLoad);

                if (MessageType.HELO.equals(message.getMessageType())) {
                    isValidated = Helo.handleServerMessage(payLoad, client);
                    if (isValidated)
                        clientInfo = new ClientInfo(client, payLoad);
                }

                if (isValidated)
                    handleMessage(message);

                writer.flush();


//
//                if (line.toLowerCase().contains("pong")) {
//                    queue.put(line);
//                }
//
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private void handleMessage(Message message) {
        MessageType messageType = message.getMessageType();
        String payload = message.getPayload();

        switch(messageType) {
            case BCST:
                Broadcast.handleServerMessage(payload, clientInfo);
                break;
            case USERS:
                Users.handleServerMessage(clientInfo);
                break;
            case DM:
                DirectMessage.handleServerMessage(payload, clientInfo);
                break;
            case GETGROUPS:
                GetGroups.handleServerMessage(clientInfo);
                break;
            case MKGROUP:
                MakeGroup.handleServerMessage(payload, clientInfo);
                break;
            case JOIN:
                Join.handleServerMessage(payload, clientInfo);
                break;
            case KICK:
                Kick.handleServerMessage(payload, clientInfo);
                break;
            case SHOUT:
                Shout.handleServerMessage(payload, clientInfo);
                break;
            case LEAVE:
                Leave.handleServerMessage(payload, clientInfo);
                break;
            case QUIT:
                Quit.handleServerMessage(clientInfo);
                break;
        }

    }

}
