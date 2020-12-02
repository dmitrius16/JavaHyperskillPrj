package server;

import java.io.IOException;
import java.util.Objects;

import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataInputStream;
import java.io.DataOutputStream;



import common.ClientServerTasks.ServerRespond;
import common.FileDescription;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static String[] items = {"add", "get", "delete", "exit"};
    private static final String okCode = "200";
    private static final String errCode = "404";
    private static final String fileExists = "403";
    private static ExecutorService executor;
    private static volatile boolean exit = false;
    public static void main(String[] args) {
        int port = 23456;


        int poolSize = Runtime.getRuntime().availableProcessors();
        executor = Executors.newFixedThreadPool(poolSize);

        RequestExecuter.initStorage();

        try (ServerSocket server = new ServerSocket(port)) {
            while (!exit) {
                Socket socket = server.accept();
                ///### executor.submit(()->serverTask(socket));
                serverTask(socket);
            }
            RequestExecuter.saveFileNamesInfo();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static FileDescription getFileDescr(String[] requestStr) {
        FileDescription fileDescr;
        if (requestStr[1].equals("BY_ID")) {
            fileDescr = new FileDescription(Integer.parseInt(requestStr[2]));
        } else
            fileDescr = new FileDescription(requestStr[2]);
        return fileDescr;
    }

    public static void serverTask(Socket sock) {
        try {
            DataInputStream input = new DataInputStream(sock.getInputStream());
            DataOutputStream output = new DataOutputStream(sock.getOutputStream());
            String[] msg = input.readUTF().split("\\s+", 3);

            if (msg[0].equals("PUT") || msg[0].equals("GET") || msg[0].equals("DELETE")) {

                RequestExecuter reqExec = new RequestExecuter(input, output);

                if (msg[0].equals("PUT")) {
                    FileDescription fileDescr = new FileDescription(msg[1]);
                    ///### reqExec.executeRequest(fileDescr, reqExec::put);
                    executor.submit(()->reqExec.executeRequest(fileDescr, reqExec::put));
                } else {
                    FileDescription fileDescr = getFileDescr(msg);
                    if (msg[0].equals("GET")) {
                       ///### reqExec.executeRequest(fileDescr, reqExec::get);
                        executor.submit(()->reqExec.executeRequest(fileDescr, reqExec::get));
                    } else if (msg[0].equals("DELETE")) {
                       ///### reqExec.executeRequest(fileDescr, reqExec::delete);
                        executor.submit(()->reqExec.executeRequest(fileDescr, reqExec::delete));
                    }
                }

            } else if (msg[0].equals("exit")) {
                executor.shutdown();
                try {
                    boolean terminated = executor.awaitTermination(60, TimeUnit.MILLISECONDS);
                } catch (InterruptedException ex) {
                    System.out.println("Await termination error");
                }
                exit = true;
            }
        } catch (IOException ex) {
            System.out.println("Exception occur! No data received from client!");
        }
    }

}
