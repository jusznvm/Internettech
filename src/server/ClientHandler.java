package server;

import client.ClientInfo;
import message.Message;
import message.MessageType;
import server.Server;
import utils.Utils;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;

public class ClientHandler extends Thread {

    private Socket client;
    private BlockingQueue<String> queue;

    public ClientHandler(Socket client, BlockingQueue<String> queue) {
        this.client = client;
        this.queue = queue;
    }


    //TODO: iets met ClientInfo doen (:
    @Override
    public void run() {

        try {
            PrintWriter writer = new PrintWriter(client.getOutputStream());
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
                    Server.activeClients.put(client, payLoad);
                    ClientInfo clientInfo = new ClientInfo(client, payLoad);
                }

                Message response = message.handleMessage();

                writer.println(response.toString());
                writer.flush();


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

}
