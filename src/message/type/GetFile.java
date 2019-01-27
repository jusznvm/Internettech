package message.type;

import message.Message;
import model.ClientInfo;
import model.TransferRequest;
import server.TransferHandler;
import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

public class GetFile extends Message {
    public GetFile(String type, String payload) {
        super(type, payload);
    }

    public static void handleServerMessage(String payload, ClientInfo clientInfo) {
        try {
            PrintWriter writer = new PrintWriter(clientInfo.getSocket().getOutputStream());
            int transferId = Integer.valueOf(payload);

            TransferRequest transferRequest = Utils.findTransferRequest(transferId);

            if (transferRequest.getReceiver().getSocket().equals(clientInfo.getSocket())) {
                PrintWriter senderWriter = new PrintWriter(transferRequest.getSender().getSocket().getOutputStream());

                TransferHandler transferHandler = new TransferHandler(transferRequest);
                transferHandler.start();

                writer.println("GETFILE " + transferRequest.getRequestId() + " " + transferRequest.getFile() + " started");
                writer.flush();

                senderWriter.println("GETFILE " + transferRequest.getRequestId() + " " + transferRequest.getFile() + " accepted");
                senderWriter.flush();

            } else {
                writer.println("ERR This file is not meant for you");
            }

            writer.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
