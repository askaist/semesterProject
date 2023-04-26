package threads;

import java.io.BufferedReader;
import java.io.IOException;

public class MasterReaderThread extends Thread {

    BufferedReader bufferedReader;
    String clientRequest;

    public MasterReaderThread(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public String getClientRequest() {
        return clientRequest;
    }

    @Override
    public void run() {
        try {
            clientRequest = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
