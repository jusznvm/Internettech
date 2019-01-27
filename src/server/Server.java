package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static final int SERVER_PORT = 1337;
    private ServerSocket serverSocket;

    public static Set<ClientInfo> activeClients = ConcurrentHashMap.newKeySet();
    public static Map<String, List<ClientInfo>> activeGroups = new ConcurrentHashMap<>();

    public Server() {
    }

    public void start() {

        try {
            serverSocket = new ServerSocket(SERVER_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                if (socket.isConnected()) {
                    System.out.println("client connected");
                }

                BlockingQueue<String> pingPongQueue = new ArrayBlockingQueue<>(50);
                ClientHandler handler = new ClientHandler(socket, pingPongQueue);
                //PingHandler pingHandler = new PingHandler(socket, pingPongQueue);

                handler.start();
                // pingHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
