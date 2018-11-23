import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

public class PingHandler extends Thread {
    private Socket client;
    private BlockingQueue<String> queue;

    public PingHandler(Socket client, BlockingQueue<String> queue) {
        this.client = client;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            PrintWriter writer = new PrintWriter(client.getOutputStream());

            while (true) {
                writer.println("PING");
                writer.flush();
                queue.take();
                sleep(60000);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
