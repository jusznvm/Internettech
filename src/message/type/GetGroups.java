package message.type;

import model.ClientInfo;
import message.Message;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

public class GetGroups extends Message {
    public GetGroups(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(ClientInfo origin) {
        try {
            PrintWriter writer = new PrintWriter(origin.getSocket().getOutputStream());

            for (Map.Entry<String, List<ClientInfo>> entry : Server.activeGroups.entrySet()) {
                writer.println(entry.getKey());
            }

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
