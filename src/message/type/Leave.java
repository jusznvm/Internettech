package message.type;

import client.ClientInfo;
import message.Message;
import server.Server;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Leave extends Message {
    public Leave(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(String payload, ClientInfo client) {

        List<ClientInfo> groupMembers = null;
        try {
            PrintWriter writer = new PrintWriter(client.getSocket().getOutputStream());

            if (Server.activeGroups.containsKey(payload)) {
                groupMembers = Utils.getGroup(payload);

                if (Utils.isPartOfGroup(groupMembers, client.getUserName())) {
                    writer.println("OK : Succesfully left group.");
                    writer.flush();

                    groupMembers.remove(client);

                    for (ClientInfo info : groupMembers) {
                        writer = new PrintWriter(info.getSocket().getOutputStream());
                        writer.println("[" + payload + "]: " + client.getUserName() + " has left the group :(");
                        writer.flush();

                    }
                }
                else
                    writer.println("ERR : You're not part of this group");

            } else
                writer.println("ERR : Group not found !");

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}