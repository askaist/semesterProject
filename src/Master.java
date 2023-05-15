import threads.ClientThread;
import threads.MasterLogicThread;
import threads.SlaveThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Master {

    public static void main(String[] args) {
//      args = new String[] { "30121" };

//      if (args.length != 1) {
//          System.err.println("Usage: java EchoServer <port number>");
//          System.exit(1);
//      }

//      int portNumber = Integer.parseInt(args[0]);

        try (ServerSocket masterClientSocket = new ServerSocket(30121);
             ServerSocket masterSlaveASocket = new ServerSocket(30122);
             ServerSocket masterSlaveBSocket = new ServerSocket(30123)) {

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

            AtomicInteger jobsOfTypeA = new AtomicInteger(0);
            AtomicInteger jobsOfTypeB = new AtomicInteger(0);
            String jobTypeSubmitted = "";
            int id = 0;

//          Thread clientResponseThread = new Thread(() -> {
//
//          });

            while (true) {
                // Accept client connection
                Socket clientSocket = masterClientSocket.accept();
                System.out.println("New client connected");

                // Start client thread
                ClientThread clientThread = new ClientThread(clientSocket, id);
                clientThread.start();

                MasterLogicThread masterLogicThread = new MasterLogicThread(jobTypeSubmitted, clientThread, jobsOfTypeA,
                        jobsOfTypeB, slaveThreadA, slaveThreadB, id);

                masterLogicThread.start();

                id++;

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}