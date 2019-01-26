package message.type;

import message.Message;

public class Kick extends Message {
    public Kick(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
