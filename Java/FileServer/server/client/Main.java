package client;
import java.io.*;
import java.net.Socket;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.util.Scanner;

import common.ClientServer;



public class Main {
    private static final String menu = "Enter action (1 - get the file, 2 - save the file, 3 - delete the file):";
    private static final String enterFileName = "Enter name of the file: ";
    private static final String enterFileNameOnServer = "Enter name of the file to be saved on server:";
    private static final String getFileFromNameOrId = "Do you want to get the file by name or by id (1 - name, 2 - id):";
    private static final String requestSend = "The request was sent.";
    private static final String okResponseSays = "Ok, the response says that the file was ";
    private static final String errCodeUndef = "Error, undefined received code";

    private static final int CHUNK_SIZE = ClientServer.CHUNK_SIZE;

    private static Socket socket;
    private static DataInputStream input;
    private static DataOutputStream output;

    private static ClientServer rxtxTask = new ClientServer();

    private static String getFileRequest(String fileName) throws IOException {
        output.writeUTF("GET " + fileName);
        System.out.println(requestSend);

        String[] res = input.readUTF().split("\\s+");
        if (res[0].equals("404")) {
            return okResponseSays + "not found!";
        } else if (res[0].equals("200")) {
            String temp_res = "Ok, the content of the file is:\n";
            if(res.length > 1) {
                temp_res += res[1];
            }
            return temp_res;
        } else {
            return errCodeUndef;
        }
    }

    private static String deleteFileRequest(String fileName) throws IOException {
        output.writeUTF("DELETE " + fileName);
        System.out.println(requestSend);
        String res = input.readUTF();
        if (res.equals("200")) {
            return okResponseSays + "successfully deleted!";
        } else if (res.equals("404")) {
            return okResponseSays + "not found!";
        } else {
            return errCodeUndef;
        }
    }

    private static String putFileRequest(String fileName, String serverFileName) throws IOException {
        StringBuilder sb = new StringBuilder("PUT ")
                            .append(fileName)
                            .append(" ");
        String path = Paths.get("").toAbsolutePath().toString() + "\\File Server\\task\\src\\client\\data\\";

        File file = new File(path + fileName);
        String result = "";
        if (file.exists()) {
            FileInputStream readFile = new FileInputStream(file);
            output.writeUTF("PUT " + serverFileName);
            rxtxTask.transmitFile(readFile, output);
            result = input.readUTF();
        } else {
            System.out.println("File not found on the client side");
        }
        return result;
    }

    private static int inputNumber(Scanner usrInput) {
        String errMsg = "Incorrect input! You have to input number, try again.";
        while(true) {
            try {
                int result = Integer.parseInt(usrInput.nextLine());
                    return result;
            } catch (NumberFormatException ex) {
                System.out.println(errMsg);
            }
        }
    }


    private static int chooseHowGetOrDeleteFile(Scanner usrInput) {
        while(true) {
            int result = inputNumber(usrInput);
            if (result == 1 || result == 2) {
                return result;
            } else {
                System.out.println("Number must be equal either 1 or 2, try again.");
            }
        }
    }

    public static void handleMenu() {

        Scanner scanner = new Scanner(System.in);
        System.out.println(menu);
        try
        {
            int menuItem = Integer.parseInt(scanner.nextLine());

            switch(menuItem) {
                case 1:
                    System.out.println(getFileFromNameOrId);
                    int item = chooseHowGetOrDeleteFile(scanner);
                    System.out.printf("Enter %s: ", item == 1 ? "name" : "id");
                    if (item == 2) {
                        Integer id = inputNumber(scanner);
                        getFileRequest(id.toString())
                    }
                    break;
                case 2:
                    System.out.print(enterFileName);
                    String fileName = scanner.nextLine();
                    System.out.print(enterFileNameOnServer);
                    String serverFileName = scanner.nextLine();
                    String serverAnswer = putFileRequest(fileName, serverFileName);
                    String[] res = serverAnswer.split("\\s+");
                    if (res[0].equals("200")) {
                        System.out.println("Response says that file is saved! ID = " + res[1]);
                    }
                    break;
                case 3:
                    //result = deleteFileRequest(fileName);
                    break;
                default:
                    System.out.println("Choose correct item!");
                    break;
            }
        }
        catch (IOException ex) {
                ex.printStackTrace();
        }

    }

    public static void main(String[] args) {
        String address = "127.0.0.1";
        int port = 23456;

        try {
            socket = new Socket(InetAddress.getByName(address), port);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            HandleMenu();

            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
