package message;

import message.type.Broadcast;
import message.type.DirectMessage;
import message.type.Helo;
import message.type.Quit;

public class Message {
    private MessageType messageType;
    private String type;
    private String payload;

    public Message(String type, String payload) {
        this.type = type;
        this.payload = payload;

        setMessageType(type);
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getPayload() {
        return payload;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    @Override
    public String toString() {
        return messageType + " " + payload;
    }

    public Message handleMessage() {
        switch(messageType) {
            case HELO:
                return Helo.handleServerMessage(payload);
            case BCST:
                return Broadcast.handleServerMessage(payload);
            case DM:
                return DirectMessage.handleServerMessage(payload);
            case QUIT:
                return Quit.handleServerMessage(payload);
        }
        return null;
    }

    private void setMessageType(String type) {
        for (MessageType t : MessageType.values()) {
            if (t.toString().equalsIgnoreCase(type))
                this.messageType = t;
        }
    }
}
