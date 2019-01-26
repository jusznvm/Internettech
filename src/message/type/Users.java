package message.type;

import message.Message;

public class Users extends Message {
    public Users(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
