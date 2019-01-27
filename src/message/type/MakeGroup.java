package message.type;

import server.ClientInfo;
import message.Message;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MakeGroup extends Message {
    public MakeGroup(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(String payload, ClientInfo origin) {
        String group = payload.split(" ")[0];

        try {
            PrintWriter writer = new PrintWriter(origin.getSocket().getOutputStream());

            if (Server.activeGroups.containsKey(group))
                writer.println("ERR group name '" + group + "' already taken!");
            else {
                List<ClientInfo> groupMembers = Collections.synchronizedList(new ArrayList<>());

                groupMembers.add(origin);
                Server.activeGroups.putIfAbsent(group, groupMembers);

                writer.println("OK " + group + " created");
            }

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
