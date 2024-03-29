package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

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
                sleep(60000);
                writer.println("PING");
                writer.flush();
                if (queue.poll(3, TimeUnit.SECONDS) == null) {
                    writer.println("DSCN Pong timeout");
                    client.close();
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
