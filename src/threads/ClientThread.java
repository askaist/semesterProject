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

    public ClientThread(Socket client) {
        this.client = client;
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

//    public synchronized String getResponse() {
//        while (taskCompleted == null) {
//            try {
//                wait(); // wait until response is available
//            } catch (InterruptedException e) {
//                // handle the exception
//            }
//        }
//        String result = response;
//        response = null;
//        return result;
//    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            System.out.println("Client thread started for " + client.getInetAddress().getHostAddress());

//            while (true) {
//                synchronized (this) {
//                    while (jobType == null) {
//                        System.out.println("waiting for job");
//                        wait(); // wait until request is available
//                    }
//                    System.out.println("job type " + jobType);
//                    out.println(jobType);
//                    jobType = null;
//                }
//                String response = in.readLine();
//                synchronized (this) {
//                    this.response = response;
//                    notify(); // notify the thread that response is available
//                }
//            }

            System.out.println("waiting for job");
            jobType = in.readLine();
            System.out.println("job received of type " + jobType);

            System.out.println("waiting on job completion");
            while (!jobCompleted) {
                Thread.sleep(200);
            }

            out.println("task completed");





        } catch (IOException | InterruptedException e) {
            System.err.println("Error in client thread for " + client.getInetAddress().getHostAddress() + ": " + e.getMessage());
        }
    }
}
