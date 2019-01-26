package message.type;

import message.Message;

public class GetGroups extends Message {
    public GetGroups(String type, String payload) {
        super(type, payload);
    }

    public static Message handleServerMessage(String payload) {
        return null;
    }
}
