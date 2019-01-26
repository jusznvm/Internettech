package message.type;

import message.Message;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Users extends Message {
    public Users(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(Socket origin) {
        try {
            PrintWriter writer = new PrintWriter(origin.getOutputStream());
            writer.println(Server.activeClients.values());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
