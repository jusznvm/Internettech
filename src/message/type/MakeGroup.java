package message.type;

import message.Message;

public class MakeGroup extends Message {
    public MakeGroup(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
