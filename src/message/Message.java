package message;

public class Message {
    private MessageType messageType;
    private String payload;

    public Message(String type, String payload) {
        this.payload = payload;
        this.messageType = setMessageType(type);
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

    private MessageType setMessageType(String type) {
        for (MessageType t : MessageType.values()) {
            if (t.toString().equalsIgnoreCase(type))
                return t;
        }
        return null;
    }
}
