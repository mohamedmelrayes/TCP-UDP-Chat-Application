//package dualchatapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DualChatApp {

    public static void main(String[] args) {
        new Thread(() -> startServer(5001)).start();
        new Thread(() -> startClient("127.0.0.1", 5002)).start();
    }

    private static void startServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server listening on port " + port + "...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket);

            DataInputStream dataIn = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(clientSocket.getOutputStream());

            startMessagingThreads(dataIn, dataOut);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startClient(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            System.out.println("Connected to server on port " + port);

            DataInputStream dataIn = new DataInputStream(socket.getInputStream());
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());

            startMessagingThreads(dataIn, dataOut);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void startMessagingThreads(DataInputStream dataIn, DataOutputStream dataOut) {
        new Thread(() -> {
            try {
                while (true) {
                    String receivedMessage = dataIn.readUTF();
                    System.out.println("Other: " + receivedMessage);

                    if (receivedMessage.equalsIgnoreCase("exit")) {
                        System.out.println("Chat ended by the other side.");
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    System.out.print("You: ");
                    String userMessage = new java.util.Scanner(System.in).nextLine();
                    dataOut.writeUTF(userMessage);

                    if (userMessage.equalsIgnoreCase("exit")) {
                        System.out.println("Chat ended by you.");
                        System.exit(0);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
