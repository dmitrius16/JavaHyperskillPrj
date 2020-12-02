package client;
import java.io.*;
import java.net.Socket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import common.ClientServerTasks;

public class Main {
    private static final String menu = "Enter action (1 - get the file, 2 - save the file, 3 - delete the file):";
    private static final String enterFileName = "Enter name of the file: ";
    private static final String enterFileNameOnServer = "Enter name of the file to be saved on server: ";
    private static final String getFileFromNameOrId = "Do you want to get the file by name or by id (1 - name, 2 - id):";
    private static final String deleteFileFromNameOrId = "Do you want to delete the file by name or by id (1 - name, 2 - id):";
    private static final String requestSend = "The request was sent.";
    private static final String responseSays = "The response says that the file ";
    private static final String fileNotFoundResponseSays = "The response says that this file is not found!";
    private static final String fileDownloadedTypeFileName = "The file was downloaded! Specify a name for it: ";
    private static final String undefErrOccur = "Undefined IOException error occur";
    private static final String fileSucessfullySaved = "File saved on the hard drive!";

    private static final int CHUNK_SIZE = ClientServerTasks.CHUNK_SIZE;

    private static Socket socket;
    private static DataInputStream input;
    private static DataOutputStream output;

    private static ClientServerTasks rxtxTask = new ClientServerTasks();

    private static String getFileNamePath(String fileName) {
        //return Paths.get("").toAbsolutePath().toString() + "\\File Server\\task\\src\\client\\data\\" + fileName;
        return "c:\\Users\\Dmitriy\\IdeaProjects\\File Server\\File Server\\task\\src\\client\\data\\" + fileName;
    }

    private static String createRequest(String request, String fileName ,boolean isNameID) {
        return request + " " + (isNameID ? "BY_ID " : "BY_NAME ") + fileName;
    }

    private static void getFileRequest(String fileName, boolean isNameID, Scanner scanner) throws IOException {
        //output.writeUTF("GET " + (isNameID ? "BY_ID " : "BY_NAME ") + fileName);
        output.writeUTF(createRequest("GET", fileName, isNameID));
        System.out.println(requestSend);
        String serverRespond = input.readUTF();
        if (serverRespond.equals("200")) {
            File file = new File(getFileNamePath("temp.tmp"));
            try (FileOutputStream fileOutput = new FileOutputStream(file)) {
                rxtxTask.receiveFile(fileOutput, input);
            } catch(IOException ex) {
                System.out.println(undefErrOccur);
            }

            System.out.print(fileDownloadedTypeFileName);
            String clientFileName = scanner.nextLine();
            Path filePath = Path.of(getFileNamePath("temp.tmp"));
            Files.move(filePath, filePath.resolveSibling(clientFileName));
            System.out.println(fileSucessfullySaved);
        } else {
            System.out.println(fileNotFoundResponseSays);
        }
    }

    private static void deleteFileRequest(String fileName, boolean isNameID) throws IOException {
        output.writeUTF(createRequest("DELETE", fileName, isNameID));
        System.out.println(requestSend);
        String res = input.readUTF();
        if (res.equals("200")) {
            System.out.println(responseSays + "was deleted successfully!");
        } else if (res.equals("404")) {
            System.out.println(responseSays + "is not found!");
        } else {
            System.out.println(responseSays + "undefined error occur!");
        }

    }

    private static void turnOffServerRequest() throws IOException {
        output.writeUTF("exit");
    }

    private static void putFileRequest(String fileName, String serverFileName) throws IOException {
        StringBuilder sb = new StringBuilder("PUT ")
                            .append(fileName)
                            .append(" ");

        File file = new File(getFileNamePath(fileName));

        if (file.exists()) {
            FileInputStream readFile = new FileInputStream(file);
            output.writeUTF("PUT " + serverFileName);
            rxtxTask.transmitFile(readFile, output);
            String[] respond = input.readUTF().split("\\s+",2);
            if (respond[0].equals("200")) {
                System.out.println("Response says that file is saved! ID = " + respond[1]);
            } else if (respond[0].equals("403")) {
                System.out.println("Response says that file with this name is already exists");
            } else {
                System.out.println("Response says that undefined error occur!");
            }
        } else {
            System.out.println("File not found on the client side");
        }
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

    private static boolean isUsrChooseFileByID(String prompt, Scanner scanner) {
        System.out.println(prompt);
        int item = chooseHowGetOrDeleteFile(scanner);
        boolean isGetFromId = item == 2;
        System.out.printf("Enter %s: ", isGetFromId ? "id" : "name");
        return isGetFromId;
    }

    private static int chooseMenuItem(Scanner scanner) {

        while(true) {
            String usrInp = scanner.nextLine();
            if (usrInp.equals("exit")) {
                return 0;
            } else {
                try {
                    int res = Integer.parseInt(usrInp);
                    if (res >= 1 && res <= 3) {
                        return res;
                    } else {
                        System.out.println("Enter number from 1 to 3. Try again!");
                    }
                } catch (NumberFormatException ex) {
                    System.out.println("Please enter number! Try again.");
                }
            }
        }
    }

    public static void handleMenu() {

        Scanner scanner = new Scanner(System.in);
        System.out.println(menu);
        try
        {
            int menuItem = chooseMenuItem(scanner);

            switch(menuItem) {
                case 0:
                    turnOffServerRequest();
                break;
                case 1:
                    boolean isGetFromId = isUsrChooseFileByID(getFileFromNameOrId, scanner);
                    getFileRequest(scanner.nextLine(), isGetFromId, scanner);
                    break;
                case 2:
                    System.out.print(enterFileName);
                    String fileName = scanner.nextLine();
                    // check here if file not exists
                    System.out.print(enterFileNameOnServer);
                    String serverFileName = scanner.nextLine();
                    putFileRequest(fileName, serverFileName);
                    break;
                case 3:
                    isGetFromId = isUsrChooseFileByID(deleteFileFromNameOrId, scanner);
                    deleteFileRequest(scanner.nextLine(), isGetFromId);
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
            handleMenu();

            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
