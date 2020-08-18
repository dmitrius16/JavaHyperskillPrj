package client;
import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;

public class Main {
    private static final String menu = "Enter action (1 - get the file, 2 - create a file, 3 - delete the file):";
    private static final String enterFileName = "Enter file name: ";
    private static final String requestSend = "The request was sent.";
    private static final String okResponseSays = "Ok, the response says that the file was ";
    private static final String errCodeUndef = "Error, undefined received code";

    private static Socket socket;
    private static DataInputStream input;
    private static DataOutputStream output;

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

    private static String putFileRequest(String fileName, String fileContent) throws IOException {
        StringBuilder sb = new StringBuilder("PUT ")
                            .append(fileName)
                            .append(" ")
                            .append(fileContent);
        output.writeUTF(sb.toString());
        System.out.println(requestSend);
        String res = input.readUTF();
        if (res.equals("200")) {
            return okResponseSays + "created!";
        } else if (res.equals("404")) {
            return "Ok, the response says that creating the file was forbidden!";
        } else {
            return errCodeUndef;
        }
    }



    public static void HandleMenu() {

        Scanner scanner = new Scanner(System.in);
        System.out.println(menu);
        try
        {
            int menuItem = Integer.parseInt(scanner.nextLine());
            System.out.print(enterFileName);
            String fileName = scanner.nextLine();
            String result;
            switch(menuItem) {
                case 1:
                    result = getFileRequest(fileName);
                    break;
                case 2:
                    result = deleteFileRequest(fileName);
                    break;
                case 3:
                    String fileCont = scanner.nextLine();
                    result = putFileRequest(fileName, fileCont);
                    break;
                default:
                    result = "Choose correct item!";
                    break;
            }
            System.out.println(result);
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
