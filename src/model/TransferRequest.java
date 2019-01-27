package model;

public class TransferRequest {
    private int requestId;
    private ClientInfo sender;
    private ClientInfo receiver;

    private String file;

    public TransferRequest(ClientInfo sender, ClientInfo receiver, String file, int requestId) {
        this.sender = sender;
        this.receiver = receiver;
        this.file = file;
        this.requestId = requestId;
    }

    public int getRequestId() {
        return requestId;
    }

    public ClientInfo getReceiver() {
        return receiver;
    }

    public ClientInfo getSender() {
        return sender;
    }

    public String getFile() {
        return file;
    }
}
