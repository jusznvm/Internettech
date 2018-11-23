import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static final int SERVER_PORT = 1337;
    private ServerSocket serverSocket;

    private Map<Integer, Socket> clientMap = new HashMap<>();

    public Server() {
    }

    public void start() {

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while(true) {
            try {
                Socket socket = serverSocket.accept();
                if(socket.isConnected()) {
                    System.out.println("client connected");
                }
                clientMap.put(socket.getPort(), socket);
                ClientHandler handler = new ClientHandler(socket);
                handler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pingClient() {

    }
}
