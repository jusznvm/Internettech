package message.type;

import model.ClientInfo;
import message.Message;
import model.TransferRequest;
import server.Server;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

public class DMFile extends Message {

    public DMFile(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(String payload, ClientInfo client) {
        String userName = payload.split(" ")[0];
        String message = payload.split(" ")[1];

        try {
            PrintWriter clientWriter = new PrintWriter(client.getSocket().getOutputStream(), true);

            ClientInfo recipient = Utils.findUserFromActiveClients(userName);

            if (recipient != null) {
                PrintWriter writer = new PrintWriter(recipient.getSocket().getOutputStream(), true);
                clientWriter = new PrintWriter(client.getSocket().getOutputStream(), true);

                TransferRequest request = new TransferRequest(client, recipient, message, Server.getTransferId());
                Server.transferRequests.add(request);
                
                writer.println(client.getUserName() + " wants to send you " + message + ". Accept with 'GETFILE " + request.getRequestId() + "'");
                clientWriter.println("OK request sent to " + recipient.getUserName());
            } else {
                clientWriter.println("ERR user not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
