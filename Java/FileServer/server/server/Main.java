package server;
import java.io.IOException;
import java.util.Scanner;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Main {
    private static String[] items = {"add", "get", "delete", "exit"};
    private static final String okCode = "200";
    private static final String errCode = "403";
    /*
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
    }*/

    public static void main(String[] args) {
        int port = 23456;
        System.out.println("Server started!");
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        FileStorage fStorage = new FileStorage();
        boolean exit = false;
        try (ServerSocket server = new ServerSocket(port)) {
            while(!exit) {
                try (Socket socket = server.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())
                ) {
                    String[] msg = input.readUTF().split("//s+", 3);
                    boolean serverResult = true;
                    if (msg[0].equals("PUT")) {
                        System.out.println("Receive PUT cmd");
                        serverResult = fStorage.put(msg[1], msg[2]);
                    } else if (msg[0].equals("GET")) {
                        System.out.println("Receive GET cmd");
                        FileStorage.FileContent cont = new FileStorage.FileContent();
                        serverResult = fStorage.get(msg[1], cont);
                    } else if (msg[0].equals("DELETE")) {
                        System.out.println("Receive DELETE cmd");
                        serverResult = fStorage.delete(msg[1]);
                    } else if (msg[0].equals("exit")) {
                        System.out.println("Receive exit cmd");
                        exit = true;
                    }

                    if (serverResult)
                        output.writeUTF(okCode);
                    else
                        output.writeUTF(errCode);
                }
            }
        } catch (IOException ex)    {
            ex.printStackTrace();
        }
    }
}
