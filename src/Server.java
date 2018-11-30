import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static final int SERVER_PORT = 1337;
    private ServerSocket serverSocket;

    private static Set<Socket> activeClients = ConcurrentHashMap.newKeySet();
    private BlockingQueue<String> pingPongQueue = new ArrayBlockingQueue<>(50);

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
                ClientHandler handler = new ClientHandler(socket, pingPongQueue);
                PingHandler pingHandler = new PingHandler(socket, pingPongQueue);

                handler.start();
                pingHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendToAll(String message, Socket socket, String userName) {
        for (Socket client : activeClients) {
            try {
                PrintWriter writer = new PrintWriter(client.getOutputStream());

                //TODO: encode message ?
                if (client == socket) {
                    writer.println("+OK BASE64(MD5(BCST " + message + " )");
                } else {
                    writer.println(userName + ": " + message);
                }

                writer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void disconnect(Socket socket) {
        activeClients.remove(socket);
    }

}
