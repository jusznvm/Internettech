package utils;

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
        userName = userName.substring(5, userName.length());
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

}
