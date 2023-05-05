import threads.ClientRequestThread;
import threads.ClientResponseThread;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        args = new String[] { "127.0.0.1", "30121" };

        if (args.length != 2) {
            System.err.println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        String jobType;
        String response;

        try (Socket clientSocket = new Socket(hostName, portNumber);
             PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader responseReader = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connection established");

            //Get input from client
            System.out.println("Enter a job value: A or B ");
            jobType = scanner.nextLine();
            // sends message to server
            requestWriter.println(jobType);

            if (!Objects.equals(jobType, "A") && !Objects.equals(jobType, "B")) {
                System.err.println("Invalid job type submitted");
                System.exit(1);
            }
            Thread requestWriterThread = new ClientRequestThread(requestWriter, jobType);
            ClientResponseThread responseReaderThread = new ClientResponseThread(responseReader);

            requestWriterThread.start();
            responseReaderThread.start();


            requestWriterThread.join();
            responseReaderThread.join();
            response = responseReaderThread.getResponse();

            System.out.println(response);


//            String masterResponse = (ClientResponseThread) responseReaderThread.getResponse();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
