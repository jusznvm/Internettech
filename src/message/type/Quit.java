package message.type;

import server.ClientInfo;
import message.Message;
import server.Server;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

public class Quit extends Message {
    public Quit(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(ClientInfo origin) {
        try {
            PrintWriter writer = new PrintWriter(origin.getSocket().getOutputStream());

            writer.println(new Message("OK", "goodbye forever").toString());
            writer.flush();

            Server.activeClients.remove(origin);
            origin.getSocket().close();
            Utils.removeUsername(origin.getUserName());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
