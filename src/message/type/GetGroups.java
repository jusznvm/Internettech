package message.type;

import message.Message;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class GetGroups extends Message {
    public GetGroups(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(Socket origin) {
        try {
            PrintWriter writer = new PrintWriter(origin.getOutputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
