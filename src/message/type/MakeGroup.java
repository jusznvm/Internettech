package message.type;

import message.Message;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MakeGroup extends Message {
    public MakeGroup(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(String payload, Socket origin) {
        String group = payload.split(" ")[0];

        try {
            PrintWriter writer = new PrintWriter(origin.getOutputStream());

            if (Server.activeGroups.containsKey(group))
                writer.println("ERR group name '" + group + "' already taken!");
            else {
                List<String> groupMembers = Collections.synchronizedList(new ArrayList<>());
                groupMembers.add(Server.activeClients.get(origin));
                Server.activeGroups.putIfAbsent(group, groupMembers);

                writer.println("OK " + group + " created");
            }

            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
