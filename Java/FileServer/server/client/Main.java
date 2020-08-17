package client;
import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
public class Main {
    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;
        try(Socket socket = new Socket(InetAddress.getByName(address), port);
        DataInputStream input = new DataInputStream(socket.getInputStream());
        DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            System.out.println("Client started!");
            String txStr = "Give me everything you have!";
            System.out.println("Sent: " + txStr);
            output.writeUTF(txStr);
            System.out.println("Received: " + input.readUTF());
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
