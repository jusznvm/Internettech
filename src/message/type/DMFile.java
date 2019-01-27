package message.type;

import server.ClientInfo;
import message.Message;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

public class DMFile extends Message {

    public DMFile(String type, String payload) {
        super(type, payload);
    }

//    public static void handleServerMessage(String payload, ClientInfo client) {
//
//        PrintWriter writer;
//        try {
//            String userName = payload.split(" ")[0];
//            String message = payload.split(" ")[1];
//
//            ClientInfo clientInfo = Utils.findUserFromActiveClients(userName);
//            if (clientInfo != null) {
//                writer = new PrintWriter(clientInfo.getSocket().getOutputStream());
//                writer.println("DMFILE " + message);
//                writer.flush();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void handleServerMessage(String payload, ClientInfo client) {
        String userName = payload.split(" ")[0];
        String message = payload.split(" ")[1];

        PrintWriter writer;
        try {
            ClientInfo clientInfo = Utils.findUserFromActiveClients(userName);

            if(clientInfo != null) {
                writer = new PrintWriter(clientInfo.getSocket().getOutputStream(), true);
                writer.println(client.getUserName() + " wants to send you a file with name: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
