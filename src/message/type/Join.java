package message.type;

import client.ClientInfo;
import message.Message;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;

public class Join extends Message {
    public Join(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(String payload, ClientInfo origin) {

        try {
            PrintWriter writer = new PrintWriter(origin.getSocket().getOutputStream());

            if (Server.activeGroups.containsKey(payload)) {
                Server.activeGroups.get(payload).add(origin);
                writer.println("OK successfully joined group `" + payload + "`");
            }

            writer.flush();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
