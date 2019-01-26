package utils;

import client.ClientMessage;
import message.MessageType;
import client.ServerMessage;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static ArrayList<String> usedNames = new ArrayList<>();

    public static String hashMessage(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = message.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digested = md.digest(bytes);
        return new String(Base64.getEncoder().encode(digested));
    }

    public static boolean validateUsername(String userName) {
        Pattern p = Pattern.compile("[A-Za-z_0-9]+");
        Matcher m = p.matcher(userName);
        return m.matches();
    }

    public static boolean nameIsUsed(String userName) {
        for (String u : usedNames) {
            if(u.equals(userName))
                return true;
        }
        return false;
    }

    public static void addUserName(String userName) {
        usedNames.add(userName);
    }

    public static void removeUsername(String userName) {
        usedNames.remove(userName);
    }

    public static boolean validateServerMessage(final ClientMessage clientMessage, final ServerMessage serverMessage) {
        boolean isValid = false;
        try {
            final byte[] hash = MessageDigest.getInstance("MD5").digest(clientMessage.toString().getBytes());
            final String encodedHash = new String(Base64.getEncoder().encode(hash));
            if (serverMessage.getMessageType().equals(MessageType.OK) && encodedHash.equals(serverMessage.getPayload())) {
                isValid = true;
            }
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}
