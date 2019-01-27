package client;

import java.math.BigInteger;
import java.net.Socket;
import java.security.PublicKey;

public class ClientInfo {
    private Socket socket;
    private String userName;
    private PublicKey publicKey;

    public ClientInfo(Socket socket, String userName, PublicKey publicKey) {
        this.socket = socket;
        this.userName = userName;
        this.publicKey = publicKey;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUserName() {
        return userName;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
