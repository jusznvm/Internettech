package message.type;

import message.Message;

public class Unknown extends Message {
    public Unknown(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
