package message.type;

import message.Message;

public class Ok extends Message {
    public Ok(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
