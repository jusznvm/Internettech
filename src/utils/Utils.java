package utils;

import model.ClientInfo;
import model.TransferRequest;
import server.Server;

import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
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

    public static ClientInfo findUserFromActiveClients(String user){
        for (ClientInfo entry : Server.activeClients) {
            if (entry.getUserName().equalsIgnoreCase(user)) {
                return entry;
            }
        }
        return null;
    }

    public static ClientInfo findUserFromActiveClients(Socket user){
        for (ClientInfo entry : Server.activeClients) {
            if (entry.getSocket().equals(user)) {
                return entry;
            }
        }
        return null;
    }

    public static TransferRequest findTransferRequest(int id){
        for (TransferRequest entry : Server.transferRequests) {
            if (entry.getRequestId() == id) {
                return entry;
            }
        }
        return null;
    }

    public static List<ClientInfo> getGroup(String groupName) {
        return Server.activeGroups.get(groupName);
    }

    public static boolean isPartOfGroup(List<ClientInfo> group, String user) {
        for (ClientInfo info : group) {
            if (info.getUserName().equals(user))
                return true;
        }
        return false;
    }
}
