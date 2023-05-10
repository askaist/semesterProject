import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SlaveB {
    public static final String PREFERRED_WORK = "B";
    public static final String NOT_PREFERRED_WORK = "A";
    public static final int SLEEP_PREFERRED = 2000;
    public static final int SLEEP_NON_PREFERRED = 10000;


    public static Socket connectToServer(String host, int port) throws IOException {
        return new Socket(host, port);
    }

    public static void doWorkIndefinitely(Socket connectionToServer) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(connectionToServer.getInputStream()));
        PrintWriter pw = new PrintWriter(connectionToServer.getOutputStream());
        int jobID;
        System.out.println("slave B connected");
        String jobType;
        while (true) {
            while ((jobType = br.readLine()) != null) {
                jobID = Integer.parseInt(br.readLine());
                System.out.println("Received jobType "+ jobType + " ID: " + jobID);
                System.out.println("doing job");
                sleepAfterWork((jobType));
                System.out.println("job ID " + jobID + " completed");
                pw.println("job completed");
                pw.flush();
            }
        }

    }

    public static void sleepAfterWork(String jobType) {
        try {
            if (jobType.equals(PREFERRED_WORK)) {
                System.out.println("Sleeping for: "+SLEEP_PREFERRED);
                Thread.sleep(SLEEP_PREFERRED);
            } else if (jobType.equals(NOT_PREFERRED_WORK)) {
                System.out.println("Sleeping for: "+SLEEP_NON_PREFERRED);
                Thread.sleep(SLEEP_NON_PREFERRED);
            } else {
                throw new IllegalArgumentException("Job type: " + jobType + " is unrecognized");
            }
        } catch (InterruptedException ignore) {
        }
        System.out.println("Waking");
    }

    public static void main(String[] args) throws IOException {
//        String host = args[0];
//        int port = Integer.parseInt((args[1]));
        String host = "127.0.0.1";
        int port = 30123;
        Socket connectionToServer = connectToServer(host, port);
        doWorkIndefinitely(connectionToServer);
    }



}