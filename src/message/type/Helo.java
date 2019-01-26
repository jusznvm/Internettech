package message.type;

import message.Message;
import utils.Utils;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Helo extends Message {

    public Helo(String type, String payload) {
        super(type, payload);
    }

    //TODO: HELO mag alleen verstuurt worden zolang de client nog niet geaccepteerd is
    public static Message handleServerMessage(String payload) {
        Message msg;

        if (Utils.validateUsername(payload)) {
            if (Utils.nameIsUsed(payload)) {
                msg = new Message("ERR", "user already logged in");

            } else {
                Utils.addUserName(payload);
                String userHash = null;
                try {
                    userHash = Utils.hashMessage(payload);
                } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                msg = new Message("OK", userHash);
            }
        } else {
            msg = new Message("ERR", "username has an invalid format");
        }

        return msg;
    }
}
