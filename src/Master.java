import threads.MasterReaderThread;
import threads.MasterResponseThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Master {

    public static void main(String[] args) {
        args = new String[] { "30121" };

        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);
        System.out.println("Server is Up");

        try (
                ServerSocket masterServerSocket = new ServerSocket(portNumber);
                Socket clientSocket = masterServerSocket.accept();
                PrintWriter clientResponseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader clientRequestReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {



            MasterReaderThread masterReaderThread = new MasterReaderThread(clientRequestReader);
            masterReaderThread.start();
            masterReaderThread.join();





            String clientRequest = masterReaderThread.getClientRequest();
            System.out.println(clientRequest);

            MasterResponseThread masterResponseThread = new MasterResponseThread(clientResponseWriter, "hello");
            masterResponseThread.start();
            masterResponseThread.join();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }


}
