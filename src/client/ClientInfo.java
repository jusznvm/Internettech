package client;

import java.net.Socket;

public class ClientInfo {
    private Socket socket;
    private String userName;

    public ClientInfo(Socket socket, String userName) {
        this.socket = socket;
        this.userName = userName;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUserName() {
        return userName;
    }
}
