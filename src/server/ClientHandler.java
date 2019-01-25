package server;

import server.Server;
import utils.Utils;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.BlockingQueue;

public class ClientHandler extends Thread {

    private Socket client;
    private BlockingQueue<String> queue;
    private String userName;

    public ClientHandler(Socket client, BlockingQueue<String> queue) {
        this.client = client;
        this.queue = queue;
    }

    @Override
    public void run() {

        try {
            PrintWriter writer = new PrintWriter(client.getOutputStream());

            writer.println("HELO");
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (true) {
                String line = reader.readLine();
                System.out.println("read input: " + line);

                if (line.toLowerCase().contains("pong")) {
                    queue.put(line);
                }
                
                if(line.startsWith("HELO")) {
                    userName = line;
                    if(Utils.validateUsername(userName)) {
                        if(Utils.nameIsUsed(userName)) {
                            writer.println("-ERR user already logged in");
                            writer.flush();
                        } else {
                            Utils.addUserName(userName);
                            String userHash = Utils.hashMessage(userName);
                            writer.println("+OK " + userHash);
                            writer.flush();
                        }
                    } else {
                        writer.println("-ERR username has an invalid format");
                        writer.flush();
                    }

                }

                if(line.toLowerCase().startsWith("dm")) {
                    writer.println("testetst");
                    writer.flush();
                }

                if(line.toLowerCase().startsWith("quit")) {
                    writer.println("+OK Goodbye");
                    writer.flush();
                    Server.disconnect(client);
                    Utils.removeUsername(userName);
                    client.close();
                }

                Server.sendToAll(line, client, userName);
            }


        } catch (IOException | InterruptedException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
