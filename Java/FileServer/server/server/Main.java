package server;
import java.io.IOException;
import java.util.Scanner;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Main {
    private static String[] items = {"add", "get", "delete", "exit"};
    public static void handleMenuItems(FileStorageSimulator fileStorage) {
        Scanner scanner = new Scanner(System.in);
        boolean exec = true;
        while (exec) {
            String[] usrInp = scanner.nextLine().split("\\s+");
            String res = "";
            switch (usrInp[0]) {
                case "add":
                    res = fileStorage.add(usrInp[1]);
                    break;
                case "get":
                    res = fileStorage.get(usrInp[1]);
                    break;
                case "delete":
                    res = fileStorage.delete(usrInp[1]);
                    break;
                case "exit":
                    exec = false;
                    break;
            }
            System.out.println(res);
        }
    }

    public static void main(String[] args) {
        int port = 23456;
        System.out.println("Server started!");
        FileStorage fStorage = new FileStorage();
        try (ServerSocket server = new ServerSocket(port)) {
            try (Socket socket = server.accept();
                DataInputStream input = new DataInputStream(socket.getInputStream());
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())
            ) {
                String msg = input.readUTF();

                System.out.println("Received: " + msg);
                String outMsg = "All files were sent!";
                System.out.println("Sent: " + outMsg);
                output.writeUTF(outMsg);
            }
        } catch (IOException ex)    {
            ex.printStackTrace();
        }
    }
}
