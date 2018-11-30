import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
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
            PrintWriter writer = new PrintWriter(client.getOutputStream());

            writer.println("HELO");
            writer.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (true) {
                String line = reader.readLine();
                System.out.println("read input: " + line);

                if (line.toLowerCase().contains("pong")) {
                    queue.put(line);
                }
                
                if(line.startsWith("HELO")) {
                    //TODO: Username validation
                    String userHash = hashUsername(line);
                    writer.println("+OK " + userHash);
                    writer.flush();
                }
                if(line.toLowerCase().startsWith("quit")) {
                    writer.println("+OK Goodbye");
                    writer.flush();
                    client.close();
                }

            }


        } catch (IOException | InterruptedException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String hashUsername(String userName) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] bytes = userName.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digested = md.digest(bytes);

        return new String(Base64.getEncoder().encode(digested));
    }

}
