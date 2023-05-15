import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

public class Client {

    public static void main(String[] args) {

        args = new String[]{"127.0.0.1", "30121"};

        if (args.length != 2) {
            System.err.println("Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        AtomicReference<String> jobType = new AtomicReference<>();

        try (Socket clientSocket = new Socket(hostName, portNumber);
             PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader responseReader = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Connection established");

            while (true) {
                System.out.println("Enter a job value: A or B ");
                jobType.set(scanner.nextLine());

                if (Objects.equals(jobType.get(), "A") || Objects.equals(jobType.get(), "B")) {
                    break;
                } else {
                    System.err.println("Invalid job type submitted. Please try again.");
                }
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