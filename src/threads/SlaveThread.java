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



    public SlaveThread(Socket slave) {
        this.slave = slave;
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
            System.out.println("Slave thread started for " + slave.getInetAddress().getHostAddress());



            while (jobType == null){
                Thread.sleep(200);
            }
            // Send jobType to slave
            out.println(jobType);
            System.out.println("job sent to slave");


            // Read slave requests and send responses
            System.out.println("wait for response from slave");
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
//                out.println("Response from server for request " + inputLine);
                if (inputLine.equals("job completed")) {
                    System.out.println("slave completed job");
                    jobCompleted = true;
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error in slave thread for " + slave.getInetAddress().getHostAddress() + ": " + e.getMessage());
        }
    }
}
