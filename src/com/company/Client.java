package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    public static void main(String[] args) throws IOException {

        args = new String[] { "127.0.0.1", "30121" };

        if (args.length != 2) {
            System.err.println("Usage: Java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (Socket clientSocket = new Socket(hostName, portNumber);
             PrintWriter requestWriter = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader responseReader = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));) {
            String userInput;
            String serverResponse;
            while ((userInput = stdIn.readLine()) != null) {
                requestWriter.println(userInput);
                serverResponse = responseReader.readLine();
                System.out.println("Server Responds: \"" + serverResponse + "\"");
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }

}
//testing
