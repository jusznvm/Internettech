package message;

import java.security.PublicKey;

public class Message {
    private MessageType messageType;
    private String payload;
    private PublicKey publicKey;

    public Message(String type, String payload) {
        this.payload = payload;
        this.messageType = setMessageType(type);
    }

    public Message(String type, String payload, PublicKey publicKey) {
        this.payload = payload;
        this.messageType = setMessageType(type);
        this.publicKey = publicKey;
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

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return messageType + " " + payload;
    }

    private MessageType setMessageType(String type) {
        for (MessageType t : MessageType.values()) {
            if (t.toString().equalsIgnoreCase(type))
                return t;
        }
        return null;
    }
}
