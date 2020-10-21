package server;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Main {
    private static String[] items = {"add", "get", "delete", "exit"};
    private static final String okCode = "200";
    private static final String errCode = "404";
    private static final String fileExists = "403";

    private static String resultString(FileStorage.ServerCode result, String content) {
        String sendStr = result.getRepr();
        if (Objects.nonNull(content)) {
            sendStr += " ";
            sendStr += content;
        }
        return sendStr;
    }

    public static void main(String[] args) {
        int port = 23456;
        FileStorage fStorage = new FileStorage();
        boolean exit = false;
        try (ServerSocket server = new ServerSocket(port)) {
            while (!exit) {
                try (Socket socket = server.accept();
                     DataInputStream input = new DataInputStream(socket.getInputStream());
                     DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

                    String[] msg = input.readUTF().split("\\s+", 3);
                    FileStorage.ServerCode serverResult;
                    String result = "";
                    if (msg[0].equals("PUT")) {
                        //                    System.out.println("Receive PUT cmd");
                        serverResult = fStorage.put(msg[1], msg[2]);
                        result = resultString(serverResult, null);
                    } else if (msg[0].equals("GET")) {
                        //                    System.out.println("Receive GET cmd");
                        FileStorage.FileContent cont = new FileStorage.FileContent();
                        serverResult = fStorage.get(msg[1], cont);
                        result = resultString(serverResult, cont.getContent());

                    } else if (msg[0].equals("DELETE")) {
                        //                    System.out.println("Receive DELETE cmd");
                        serverResult = fStorage.delete(msg[1]);
                        result = resultString(serverResult, null);
                    } else if (msg[0].equals("exit")) {
                        ///System.out.println("Receive exit cmd");
                        exit = true;
                        continue;
                    }
                    output.writeUTF(result);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
