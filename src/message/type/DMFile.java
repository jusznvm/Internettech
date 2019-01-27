package message.type;

import client.ClientInfo;
import message.Message;
import utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class DMFile extends Message {

    public DMFile(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(String payload, ClientInfo client) {

        PrintWriter writer;
        try {
            String userName = payload.split(" ")[0];
            String message = payload.split(" ")[1];

            ClientInfo clientInfo = Utils.findUserFromActiveClients(userName);
            if (clientInfo != null) {
                writer = new PrintWriter(clientInfo.getSocket().getOutputStream());
                writer.println("DMFILE " + message);
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
