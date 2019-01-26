package message.type;

import message.Message;

public class Pong extends Message {
    public Pong(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
