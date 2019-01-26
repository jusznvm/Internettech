package message.type;

import message.Message;

public class Broadcast extends Message {
    public Broadcast(String type, String payload) {
        super(type, payload);
    }

    //TODO: eigen socket een ok sturen ipv bcst
    //TODO: wel naar alle clients sturen ook (:
    public static Message handleServerMessage(String payload) {
        return new Message("BCST", payload);
    }
}