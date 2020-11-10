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
/* func needed in stage 3, in stage 4 we have ServerRepond object
    private static String resultString(FileStorage.ServerCode result, String content) {
        String sendStr = result.getRepr();
        if (Objects.nonNull(content)) {
            sendStr += " ";
            sendStr += content;
        }
        return sendStr;
    }
*/
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
                    ServerRespond respond = null;
                    String result = "";
                    if (msg[0].equals("PUT")) {
                        respond = fStorage.put(msg[1], msg[2]);
                        //result = resultString(serverResult, null);
                    } else if (msg[0].equals("GET")) {
                        FileStorage.FileContent cont = new FileStorage.FileContent();
                        respond = fStorage.get(msg[1], cont);
                        //result = resultString(serverResult, cont.getContent());

                    } else if (msg[0].equals("DELETE")) {
                        respond = fStorage.delete(msg[1]);
                        //result = resultString(serverResult, null);
                    } else if (msg[0].equals("exit")) {
                        ///System.out.println("Receive exit cmd");
                        exit = true;
                        continue;
                    }
                    if(Objects.nonNull(respond)) {
                        output.writeUTF(respond.toString());
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
