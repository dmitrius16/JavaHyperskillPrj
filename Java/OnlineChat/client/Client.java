package chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;

        try {
            Socket socket = new Socket(InetAddress.getByName(address), port);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            Thread sockInputThread = new Thread(()->{
                try {
                    while(true) {
                        if (input.available() > 0) {
                            System.out.println(input.readUTF());
                        } else {
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException ex) {
                                break;
                            }
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("Exception occur in usrInputThread thread");
                }
            });
            Scanner scanner = new Scanner(System.in);
            System.out.println("Client started!");
            while(true) {
                String usrInpStr = scanner.nextLine();
                if (usrInpStr.equals("exit")) {
                    System.out.println("Receive exit command");
                    sockInputThread.interrupt();
                    try {
                        sockInputThread.join();
                    } catch (InterruptedException ex) {

                    }
                    break;
                }
                output.writeUTF(usrInpStr);
            }
            System.out.println("Exit from client");
            socket.close();
        } catch (IOException ex) {

        }
    }
}
