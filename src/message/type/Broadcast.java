package message.type;

import client.ClientInfo;
import message.Message;
import server.Server;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;

public class Broadcast extends Message {
    public Broadcast(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(String payload, ClientInfo origin) {

        for (ClientInfo entry : Server.activeClients) {
            try {
                PrintWriter writer = new PrintWriter(entry.getSocket().getOutputStream());

                if (origin.getSocket() == entry.getSocket()) {
                    String hashedMessage = Utils.hashMessage(payload);
                    writer.println("+OK BASE64(MD5(BCST " + hashedMessage + ")");
                } else {
                    writer.println("BCST " + entry.getUserName() + ": " + payload);
                }

                writer.flush();

            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }
}