package threads;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientResponseThread extends Thread {
    private BufferedReader responseReader;
    private String response;

    public ClientResponseThread(BufferedReader responseReader) {
        this.responseReader = responseReader;
    }

    public String getResponse() {
        return response;
    }

    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();
        try {
            String line;
            while ((line = responseReader.readLine()) != null) {
                sb.append(line);
                if (line.endsWith("</response>")) {
                    break;
                }
            }
            response = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

