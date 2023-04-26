package threads;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientResponseThread extends Thread {

    BufferedReader bufferedReader;
    String response;

    public ClientResponseThread(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public void run() {
        try {
            response = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
