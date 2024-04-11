// TCPClient.java

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
  public static void main(String[] args) throws IOException {
    Socket socket = new Socket();
    socket.connect(new InetSocketAddress("127.0.0.1", 5001), 1000);
    System.out.println("Connection Successful!");
    
    DataInputStream dataIn = new DataInputStream(socket.getInputStream());
    DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
    
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter a message to send to the server: ");
    String userMessage = scanner.nextLine();
        
    dataOut.writeUTF("Hello, This is coming from Client!"+"\n"+userMessage);
    String serverMessage = dataIn.readUTF();
    System.out.println(serverMessage);

    dataIn.close();
    dataOut.close();
    socket.close(); 
  }
}
