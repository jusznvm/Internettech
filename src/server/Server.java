package server;

import utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static final int SERVER_PORT = 1337;
    private ServerSocket serverSocket;

    private static Set<Socket> activeClients = ConcurrentHashMap.newKeySet();

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
                activeClients.add(socket);

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

    public static void sendToAll(String message, Socket socket, String userName) {
        for (Socket client : activeClients) {
            try {
                PrintWriter writer = new PrintWriter(client.getOutputStream());

                if (client == socket) {
                    String hashedMessage = Utils.hashMessage(message);
                    writer.println("+OK BASE64(MD5(BCST " + hashedMessage + " )");
                } else {
                    writer.println("BCST " + userName + ": " + message);
                }

                writer.flush();

            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public static void disconnect(Socket socket) {
        activeClients.remove(socket);
    }
}
