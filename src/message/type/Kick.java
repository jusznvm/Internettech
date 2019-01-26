package message.type;

import client.ClientInfo;
import message.Message;
import server.Server;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class Kick extends Message {
    public Kick(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(String payload, ClientInfo info) {
        String group = payload.split(" ")[0];
        String userToKick = payload.split(" ")[1];

        ClientInfo clientToKick = Utils.findUserFromActiveClients(userToKick);

        try {
            PrintWriter writer = new PrintWriter(info.getSocket().getOutputStream());
            PrintWriter kickWriter = new PrintWriter(clientToKick.getSocket().getOutputStream());

            if (Server.activeGroups.containsKey(group)) {
                List<ClientInfo> members = Server.activeGroups.get(group);

                if (isOwner(members, info.getUserName())) {
                    if (memberInGroup(members, userToKick)) {
                        members.remove(clientToKick);
                        writer.println("OK " + userToKick + " removed from " + group);
                        kickWriter.println("You got kicked from " + group);
                    } else
                        writer.println("ERR User not found in group!");
                } else
                    writer.println("ERR you are not the owner of the group");
            } else {
                writer.println("ERR Group not found!");
            }

            writer.flush();
            kickWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean memberInGroup(List<ClientInfo> group, String user) {
        for (ClientInfo info : group) {
            if (info.getUserName().equals(user))
                return true;
        }
        return false;
    }

    private static boolean isOwner(List<ClientInfo> group, String user) {
        return group.get(0).getUserName().equals(user);
    }
}
