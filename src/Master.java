import threads.ClientThread;
import threads.SlaveThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Master {

    public static void main(String[] args) {
//        args = new String[] { "30121" };

//        if (args.length != 1) {
//            System.err.println("Usage: java EchoServer <port number>");
//            System.exit(1);
//        }

//        int portNumber = Integer.parseInt(args[0]);

        try (
                ServerSocket masterClientSocket = new ServerSocket(30121);
                ServerSocket masterSlaveASocket = new ServerSocket(30122);
                ServerSocket masterSlaveBSocket = new ServerSocket(30123)
        ) {

            System.out.println("Master server started");


            Socket slaveA = masterSlaveASocket.accept();


            // Start slaveA thread
            SlaveThread slaveThreadA = new SlaveThread(slaveA);
            slaveThreadA.start();
            System.out.println("slave A connected");


            Socket slaveB = masterSlaveBSocket.accept();


            // Start slaveB thread
            SlaveThread slaveThreadB = new SlaveThread(slaveB);
            slaveThreadB.start();
            System.out.println("slave B connected");

            int jobsOfTypeA = 0;
            int jobsOfTypeB = 0;
            String jobTypeSubmitted;
            int id = 0;


            while (true) {


                // Accept client connection
                Socket clientSocket = masterClientSocket.accept();
                System.out.println("New client connected");

                // Start client thread
                ClientThread clientThread = new ClientThread(clientSocket, id);
                clientThread.start();

                // Wait for job type to be set
                while (clientThread.getJobType() == null) {
                    Thread.sleep(200);
                }

                jobTypeSubmitted = clientThread.getJobType();


                // Pass job type to slaveA threads
                if (jobTypeSubmitted.equals("A")) {
                    if (jobsOfTypeA <= 5) {
                        slaveThreadA.setJobType(clientThread.getJobType());
                        jobsOfTypeA++;
                        slaveThreadA.setJobID(id);
                        System.out.println("Sent job id: " + id + " to Slave A");
                    } else {
                        slaveThreadB.setJobType(clientThread.getJobType());
                        jobsOfTypeB++;
                        slaveThreadB.setJobID(id);
                        System.out.println("Sent job id: " + id + " to Slave B");
                    }

                }

                // Pass job type to slaveB threads
                if (jobTypeSubmitted.equals("B")) {
                    if (jobsOfTypeB <= 5) {
                        slaveThreadB.setJobType(clientThread.getJobType());
                        jobsOfTypeB++;
                        slaveThreadB.setJobID(id);
                        System.out.println("Sent job id: " + id + " to Slave B");
                    } else {
                        slaveThreadA.setJobType(clientThread.getJobType());
                        jobsOfTypeA++;
                        slaveThreadA.setJobID(id);
                        System.out.println("Sent job id: " + id + " to Slave A");
                    }

                }


                while (!slaveThreadA.getJobCompleted() && !slaveThreadB.getJobCompleted()) {
                    Thread.sleep(200);
                }

                clientThread.setJobCompleted(true);
                if (clientThread.getJobType().equals("A")) {
                    jobsOfTypeA--;
                }
                if (clientThread.getJobType().equals("B")) {
                    jobsOfTypeB--;
                }

                id++;


            }




        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


}
