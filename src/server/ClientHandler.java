package server;

import client.ClientInfo;
import message.Message;
import message.MessageType;
import message.type.Broadcast;
import message.type.DirectMessage;
import message.type.Helo;
import message.type.Quit;
import server.Server;
import utils.Utils;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;

public class ClientHandler extends Thread {

    private Socket client;
    private BlockingQueue<String> queue;

    private PrintWriter writer;

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

                if (message.getMessageType().equals(MessageType.HELO)) {
                    Server.activeClients.replace(client, payLoad);
                }

                handleMessage(message);

//                writer.println(response.toString());
//                writer.flush();


//
//                if (line.toLowerCase().contains("pong")) {
//                    queue.put(line);
//                }
//
//                if (line.startsWith("HELO")) {
//                    userName = line.substring(5, userName.length());
//
//                    if (Utils.validateUsername(userName)) {
//                        if (Utils.nameIsUsed(userName)) {
//                            writer.println("-ERR user already logged in");
//                            writer.flush();
//                        } else {
//                            Utils.addUserName(userName);
//                            String userHash = Utils.hashMessage(userName);
//                            writer.println("+OK " + userHash);
//                            writer.flush();
//                        }
//                    } else {
//                        writer.println("-ERR username has an invalid format");
//                        writer.flush();
//                    }
//
//                }
//
//                if (line.toLowerCase().startsWith("dm")) {
//                    writer.println("testetst");
//                    writer.flush();
//                }
//
//                if (line.toLowerCase().startsWith("quit")) {
//                    writer.println("+OK Goodbye");
//                    writer.flush();
//                    Server.disconnect(client);
//                    Utils.removeUsername(userName);
//                    client.close();
//                }
//
//                Server.sendToAll(line, client, userName);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private void handleMessage(Message message) {
        MessageType messageType = message.getMessageType();
        String payload = message.getPayload();

        switch(messageType) {
            case HELO:
                Message msg = Helo.handleServerMessage(payload);
                writer.println(msg);
                break;
            case BCST:
                Broadcast.handleServerMessage(payload, client);
                break;
            case DM:
                DirectMessage.handleServerMessage(payload, client);
                break;
            case QUIT:
                Quit.handleServerMessage(payload);
                break;
        }

        writer.flush();

    }

}
