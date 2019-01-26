package client;

import message.MessageType;

public class ClientMessage {
    private MessageType type;
    private String line;

    public ClientMessage(MessageType type, String line) {
        this.type = type;
        this.line = line;
    }

    @Override
    public String toString() {
        return type + " " + line;
    }

    public MessageType getType() {
        return type;
    }
}
