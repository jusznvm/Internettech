package message.type;

import message.Message;

public class Leave extends Message {
    public Leave(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
