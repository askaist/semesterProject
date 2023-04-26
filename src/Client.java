import threads.ClientRequestThread;
import threads.ClientResponseThread;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

public class Client {

    public static void main(String[] args) {

        String[] connectionArray = new String[]{"127.0.0.1", "30121"};

        if (connectionArray.length != 2) {
            System.err.println(
                    "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = connectionArray[0];
        int portNumber = Integer.parseInt(connectionArray[1]);

        String jobType = args[0];

        if (!Objects.equals(jobType, "a") && !Objects.equals(jobType, "b")) {
            System.err.println(
                    "Invalid job type submitted");
            System.exit(1);
        }

        try (
                Socket clientSocket = new Socket(hostName, portNumber);
                PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                ) {


            Thread requestWriterThread = new ClientRequestThread(requestWriter, jobType);
            Thread responseReaderThread = new ClientResponseThread(responseReader);



            requestWriterThread.start();
            responseReaderThread.start();
            responseReaderThread.join();
            requestWriterThread.join();


//            String masterResponse = (ClientResponseThread) responseReaderThread.getResponse();







        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
