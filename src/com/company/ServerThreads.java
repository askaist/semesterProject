package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServerThreads implements Runnable {
    private ServerSocket serverSocket = null;
    int id;

    public ServerThreads(ServerSocket s, int id) {
        serverSocket = s;
        this.id = id;
    }

    @Override
    public void run() {
        try (Socket clientSocket = serverSocket.accept();
             PrintWriter responseWriter = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader requestReader = new BufferedReader(
                     new InputStreamReader(clientSocket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));) {
            String requestString;
            while ((requestString = requestReader.readLine()) != null) {
                System.out.println(requestString + " received by listener: " + id);
            }
        } catch (IOException e) {
            System.out.println(
                    "Exception caught when trying to listen on port " + serverSocket.getLocalPort() + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
//testing