import java.io.*;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {

        args = new String[]{"127.0.0.1", "30121"};

        if (args.length != 2) {
            System.err.println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        String jobType;

        try (Socket clientSocket = new Socket(hostName, portNumber);
             PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader responseReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             Scanner scanner = new Scanner(System.in))
        {
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

            Thread clientRequestThread = new Thread(() -> {
                requestWriter.println(jobType);
                System.out.println("Job sent to server");
            });
            clientRequestThread.start();


            Thread clientResponseThread = new Thread(() -> {
                try {
                    String response = responseReader.readLine();
                    System.out.println(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            clientResponseThread.start();

            clientRequestThread.join();
            clientResponseThread.join();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
