import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class ClientHandler extends Thread {

    private Socket client;
    private BlockingQueue<String> queue;

    public ClientHandler(Socket client, BlockingQueue<String> queue) {
        this.client = client;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter writer = new PrintWriter(client.getOutputStream());

            while (true) {
                String line = reader.readLine();
                if (line.toLowerCase().contains("pong")) {
                    queue.put(line);
                }
            }


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
