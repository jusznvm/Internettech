package message.type;

import message.Message;

public class Quit extends Message {
    public Quit(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return new Message("OK", "goodbye forever");
    }
}
