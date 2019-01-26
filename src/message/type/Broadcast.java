package message.type;

import message.Message;
import server.Server;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class Broadcast extends Message {
    public Broadcast(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(String payload, Socket origin) {

        for (Map.Entry<Socket, String> entry : Server.activeClients.entrySet()) {
            try {
                PrintWriter writer = new PrintWriter(entry.getKey().getOutputStream());

                if (origin == entry.getKey()) {
                    String hashedMessage = Utils.hashMessage(payload);
                    writer.println("+OK BASE64(MD5(BCST " + hashedMessage + ")");
                } else {
                    writer.println("BCST " + entry.getValue() + ": " + payload);
                }

                writer.flush();

            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }
}