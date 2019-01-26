package message.type;

import message.Message;
import server.Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class DirectMessage extends Message {
    public DirectMessage(String type, String payload) {
        super(type, payload);
    }

    //TODO: origin user moet vermeld worden bij de dm
    public static Message handleServerMessage(String payload) {
        String recipient = payload.split(" ", 2)[0];
        String dm = payload.split(" ", 2)[1];
        Socket socket = null;

        for (Map.Entry<Socket, String> entry : Server.activeClients.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(recipient)) {
                socket = entry.getKey();
            }
        }

        try {
            assert socket != null;
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            writer.println("");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
