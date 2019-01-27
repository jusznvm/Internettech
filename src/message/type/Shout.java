package message.type;

import model.ClientInfo;
import message.Message;
import server.Server;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Shout extends Message {
    public Shout(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(String payload, ClientInfo client) {
        String groupName = payload.split(" ", 2)[0];
        String message = payload.split(" ", 2)[1];

        PrintWriter writer;
        List<ClientInfo> groupMembers = null;

        try {
            // Check if group exists.
            if (Server.activeGroups.containsKey(groupName)) {
                groupMembers = Utils.getGroup(groupName);


                if (Utils.isPartOfGroup(groupMembers, client.getUserName())) {
                    // Send the message to all users of this group.
                    for (ClientInfo info : groupMembers) {
                        writer = new PrintWriter(info.getSocket().getOutputStream());
                        writer.println("[" + groupName + "]"
                                + client.getUserName() + ": "
                                + message);
                        writer.flush();

                    }
                } else {
                    writer = new PrintWriter(client.getSocket().getOutputStream());
                    writer.println("ERR : You're not part of this group.");
                    writer.flush();

                }
            } else {
                writer = new PrintWriter(client.getSocket().getOutputStream());
                writer.println("ERR : Group not found.");
                writer.flush();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
