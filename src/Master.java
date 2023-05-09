import threads.ClientThread;
import threads.SlaveThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

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
                ServerSocket masterSlaveSocket = new ServerSocket(30122);
//                Socket clientSocket = masterClientSocket.accept();
//                Socket slaveSocket = masterSlaveSocket.accept();
//                PrintWriter clientResponseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
//                BufferedReader clientRequestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//                PrintWriter slaveRequestWriter = new PrintWriter(slaveSocket.getOutputStream(), true);
//                BufferedReader slaveResponseReader = new BufferedReader(new InputStreamReader(slaveSocket.getInputStream()));



                ) {

            System.out.println("Master server started");

            LinkedList<SlaveThread> slaves = new LinkedList<>();

            for (int i = 1; i < 3; i++) {
                Socket slave = masterSlaveSocket.accept();
                System.out.println("slave " + i + " connected: " + slave.getInetAddress().getHostAddress());

                // Start slave thread
                SlaveThread slaveThread = new SlaveThread(slave);
                slaves.add(slaveThread);
                slaveThread.start();
            }


            while (true) {
                // Accept client connection
                Socket clientSocket = masterClientSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress().getHostAddress());

                // Start client thread
                ClientThread clientThread = new ClientThread(clientSocket);
                clientThread.start();

                // Wait for job type to be set
                while (clientThread.getJobType() == null) {
                    Thread.sleep(200);
                }



                // Pass job type to slave threads
                System.out.println("sending the job to a slave");
                for (SlaveThread slaveThread : slaves) {
                    slaveThread.setJobType(clientThread.getJobType());
                }

                for (SlaveThread slaveThread : slaves) {
                    while (!slaveThread.getJobCompleted()) {
                        Thread.sleep(200);
                    }
                    clientThread.setJobCompleted(true);
                }

            }



//            MasterReaderThread masterReaderThread = new MasterReaderThread(clientRequestReader);
//            masterReaderThread.start();
//            masterReaderThread.join();





//            String clientRequest = masterReaderThread.getClientRequest();
//            System.out.println(clientRequest);

//            MasterResponseThread masterResponseThread = new MasterResponseThread(clientResponseWriter, "hello");
//            masterResponseThread.start();
//            masterResponseThread.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


}
