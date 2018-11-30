import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server {

    private static final int SERVER_PORT = 1337;
    private ServerSocket serverSocket;

    private Map<Integer, Socket> clientMap = new HashMap<>();
    private BlockingQueue<String> pingPongQueue = new ArrayBlockingQueue<>(50);

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
                ClientHandler handler = new ClientHandler(socket, pingPongQueue);
                PingHandler pingHandler = new PingHandler(socket, pingPongQueue);

                handler.start();
                pingHandler.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
