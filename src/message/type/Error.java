package message.type;

import message.Message;

public class Error extends Message {
    public Error(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
