package message.type;

import server.ClientInfo;
import message.Message;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;

public class Users extends Message {
    public Users(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(ClientInfo origin) {
        try {
            PrintWriter writer = new PrintWriter(origin.getSocket().getOutputStream());
            for (ClientInfo info : Server.activeClients) {
                writer.println(info.getUserName());
            }
            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
