package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server_Master {

    public static void main(String[] args) throws IOException {

        args = new String[] { "30121" };

        if (args.length != 1) {
            System.err.println("Usage: Java EchoServer <port number>");
            System.exit(1);
        }
        int portNumber = Integer.parseInt(args[0]);
        final int THREADS = 3;

        try(ServerSocket serverSocket = new ServerSocket(portNumber);){
            ArrayList<Thread> threads = new ArrayList<Thread>();
            for(int i = 0; i < THREADS; i++)
                threads.add(new Thread(new ServerThreads(serverSocket, i)));
            for(Thread t : threads)
                t.start();

            for(Thread t : threads) {
                try {
                    t.join();
                }catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }catch(IOException e) {
            System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
