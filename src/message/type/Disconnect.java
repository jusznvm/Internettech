package message.type;

import message.Message;

public class Disconnect extends Message {
    public Disconnect(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
