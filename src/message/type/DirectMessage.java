package message.type;

import client.ClientInfo;
import message.Message;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;


public class DirectMessage extends Message {
    public DirectMessage(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload, ClientInfo origin) {
        String recipient = payload.split(" ", 2)[0];
        String dm = payload.split(" ", 2)[1];

        ClientInfo recipientInfo = Utils.findUserFromActiveClients(recipient);

        try {
            PrintWriter writer = new PrintWriter(origin.getSocket().getOutputStream());

            if (recipientInfo != null) {
                PrintWriter rWriter = new PrintWriter(recipientInfo.getSocket().getOutputStream());

                rWriter.println("dm from " + origin.getUserName() + ": " + dm);
                rWriter.flush();

                writer.println("OK successfully dm'd!");

            } else {
                writer.println("ERR User not found!");
            }

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
