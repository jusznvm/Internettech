package client;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReceiveFileHandler extends Thread {
    private String fileType;
    private Socket transferSocket;

    public ReceiveFileHandler(String fileType, Socket transferSocket) {
        this.transferSocket = transferSocket;
        this.fileType = fileType;
    }

    @Override
    public void run() {
        try {
            String fileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + "." + fileType;

            InputStream inputStream = transferSocket.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            byte[] bytes = new byte[10000];

            int bytesRead = 0;
            while ((bytesRead = inputStream.read(bytes, 0, bytes.length)) > 0) {
                fileOutputStream.write(bytes, 0, bytesRead);
            }

            System.out.println("File received!");

            fileOutputStream.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

