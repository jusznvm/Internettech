package message.type;

import client.ClientInfo;
import message.Message;
import server.Server;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

public class Helo extends Message {

    public Helo(String type, String payload) {
        super(type, payload);
    }

    public static boolean handleServerMessage(String payload, Socket socket) {

        try {
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            if (Utils.validateUsername(payload)) {
                if (Utils.nameIsUsed(payload)) {
                    writer.println(new Message("ERR", "user already logged in, try again please").toString());

                } else {
                    Utils.addUserName(payload);
                    String userHash = null;
                    try {
                        userHash = Utils.hashMessage(payload);
                    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    writer.println(new Message("OK", userHash).toString());

                    ClientInfo clientInfo = new ClientInfo(socket, payload);
                    Server.activeClients.add(clientInfo);

                    writer.flush();

                    return true;
                }
            } else
                writer.println(new Message("ERR", "username has an invalid format").toString());

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
