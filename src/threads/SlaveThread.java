package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SlaveThread extends Thread {
    private Socket slave;
    String jobType;
    boolean jobCompleted = false;
    int jobID;


    public SlaveThread(Socket slave) {
        this.slave = slave;
    }

    public void setJobID(int jobID) {
        this.jobID = jobID;
    }

    public boolean getJobCompleted() {
        return jobCompleted;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    @Override
    public void run() {
        try (
                PrintWriter out = new PrintWriter(slave.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(slave.getInputStream()))
        ) {
            System.out.println("Slave thread started");


            while (true) {
                while (jobType == null) {
                    Thread.sleep(200);
                }

                // Send jobType to slave
                out.println(jobType);
                out.println(jobID);


                // Read slave requests and send responses
                System.out.println("wait for response from slave");
                String inputLine;
                inputLine = in.readLine();

                if (inputLine.equals("job completed")) {
                    System.out.println("slave completed job of id " + jobID);
                    synchronized (this) {
                        jobCompleted = true;
                    }
                    Thread.sleep(200);
                    jobCompleted = false;
                    jobType = null;
                }


            }


        } catch (IOException | InterruptedException e) {
            System.err.println("Error in slave thread for " + slave.getInetAddress().getHostAddress() + ": " + e.getMessage());
        }
    }
}
