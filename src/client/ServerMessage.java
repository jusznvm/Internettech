package client;

import klient.MessageType;

public class ServerMessage {
    private String line;

    public ServerMessage(String line) {
        this.line = line;
    }

    public MessageType getMessageType() {
        MessageType result = MessageType.UNKNOWN;
        try {
            if ((line != null) && (line.length() > 0)) {
                String[] splits = line.split("\\s+");
                String lineTypePart = splits[0];
                if ((lineTypePart.startsWith("-")) || (lineTypePart.startsWith("+"))) {
                    lineTypePart = lineTypePart.substring(1);
                }
                result = MessageType.valueOf(lineTypePart);
            }
        } catch (IllegalArgumentException iaex) {
            System.out.println("[ERROR] Unknown command");
        }
        return result;
    }

    public String getPayload() {
        if (getMessageType().equals(MessageType.UNKNOWN)) {
            return line;
        }

        if ((line == null) || (line.length() < getMessageType().name().length() + 1)) {
            return "";
        }

        int offset = 0;
        if ((getMessageType().equals(MessageType.OK)) || (getMessageType().equals(MessageType.ERR))) {
            offset = 1;
        }
        return line.substring(getMessageType().name().length() + 1 + offset);
    }
}
