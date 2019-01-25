import client.Client;
import server.Server;

public class App {

    public static void main(String[] args) {
        new App().run();
    }

    public void run() {
        Server server = new Server();
        server.start();
    }
}


