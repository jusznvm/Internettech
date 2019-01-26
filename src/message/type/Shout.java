package message.type;

import message.Message;

public class Shout extends Message {
    public Shout(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
