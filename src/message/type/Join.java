package message.type;

import message.Message;

public class Join extends Message {
    public Join(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
