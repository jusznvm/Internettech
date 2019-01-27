package client;


import java.io.*;
import java.net.Socket;


public class SendFileHandler extends Thread {
    private String file;
    private Socket transferSocket;

    public SendFileHandler(String file, Socket transferSocket) {
        this.file = file;
        this.transferSocket = transferSocket;
    }

    @Override
    public void run() {
        try {

            File file = new File(this.file);
            byte[] bytes = new byte[10000];

            InputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = transferSocket.getOutputStream();

            int count;
            while ((count = inputStream.read(bytes)) > 0) {
                System.out.println("Transferring bytes: " + count);
                outputStream.write(bytes, 0, count);
            }

            System.out.println("File has been sent to server");

            outputStream.close();
            transferSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
