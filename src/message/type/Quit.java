package message.type;

import message.Message;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Quit extends Message {
    public Quit(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(Socket origin) {
        try {
            PrintWriter writer = new PrintWriter(origin.getOutputStream());

            writer.println(new Message("OK", "goodbye forever").toString());
            writer.flush();

            Server.activeClients.remove(origin);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
