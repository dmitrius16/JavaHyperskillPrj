package chat.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayDeque;
import java.util.Deque;

public class Server {
    private static UserInput usrInp = new UserInput();
    private static String address = "127.0.0.1";
    private static int port = 23456;

    private static String serverGreeting = "Server started!";

    public static void main(String[] args) {
        Thread readUsrThread = new Thread(usrInp);
        readUsrThread.start();
        System.out.println(serverGreeting);
        try(ServerSocket serverSocket = new ServerSocket(port);
            Socket socket = serverSocket.accept();
            DataInputStream sockInp = new DataInputStream(socket.getInputStream());
            DataOutputStream sockOut = new DataOutputStream(socket.getOutputStream())) {

            System.out.println("Server started!");
            //Read data from client and out it to console
            Thread readInput = new Thread(() -> {
                while(true) {
                    try {
                        System.out.println(sockInp.readUTF());
                    } catch(IOException ex) {
                        break;
                    }
                }
            });
            readInput.start();

            // server output

            while(true) {
                if (usrInp.isThereUsrInput()) {
                    String input = usrInp.getUsrInput();
                    sockOut.writeUTF(input);
                    if (input.equals("exit")) {
                        System.out.println("receive exit signal from user server side");
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("IO exception when create server socket");
        }
    }
}

class UserInput implements Runnable {
    private Deque<String> usrInput;
    public UserInput() {
        usrInput = new ArrayDeque<>();
    }
    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while(true) {
                String inpStr = reader.readLine();
                putUsrInput(inpStr);
                if (inpStr.equals("exit")) {
                    break;
                }
            }
            System.out.println("Server: console read thread finished work!");
        } catch (IOException ex) {
            System.out.println(ex.getCause());
        }
    }

    public synchronized boolean isThereUsrInput() {
        return usrInput.isEmpty() == false;
    }

    public synchronized void putUsrInput(String inp) {
        usrInput.addLast(inp);
    }

    public synchronized String getUsrInput() {
        return usrInput.removeLast();
    }
}
