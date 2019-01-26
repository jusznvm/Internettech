package message.type;

import message.Message;
import server.Server;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class DirectMessage extends Message {
    public DirectMessage(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload, Socket origin) {
        String recipient = payload.split(" ", 2)[0];
        String dm = payload.split(" ", 2)[1];
        Socket recipientSocket = null;

        Map.Entry<Socket, String> recipientMap = Utils.findUserFromActiveClients(recipient);
        String senderUserName = Utils.findUserFromActiveClients(origin).getValue();

        try {
            PrintWriter writer = new PrintWriter(origin.getOutputStream());

            if (Server.activeClients.containsValue(recipient)) {
                recipientSocket = recipientMap.getKey();
                PrintWriter rWriter = new PrintWriter(recipientSocket.getOutputStream());

                rWriter.println("dm from " + senderUserName + ": " + dm);
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
