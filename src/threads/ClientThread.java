package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket client;
    String jobType;
    boolean jobCompleted = false;
    int id;

    public ClientThread(Socket client, int id) {
        this.client = client;
        this.id = id;
    }

    public void setJobCompleted(boolean jobCompleted) {
        this.jobCompleted = jobCompleted;
    }

    public synchronized void setJobType(String request) {
        this.jobType = request;
        notify(); // notify the thread that request is available
    }

    public String getJobType() {
        return jobType;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            System.out.println("Client thread started");


            System.out.println("waiting for job");
            jobType = in.readLine();


            System.out.println("job received of type " + jobType + " id: " + id);

            while (!jobCompleted) {
                Thread.sleep(200);
            }

            out.println("task completed");


        } catch (IOException | InterruptedException e) {
            System.err.println("Error in client thread for " + client.getInetAddress().getHostAddress() + ": " + e.getMessage());
        }
    }
}
