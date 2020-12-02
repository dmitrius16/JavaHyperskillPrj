package server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import common.ClientServerTasks;
import common.ClientServerTasks.ServerRespond;
import common.ClientServerTasks.ServerCode;
import common.FileDescription;
import common.CheckedConsumer;


public class RequestExecuter {
    ////private static final String folderPath = Paths.get("").toAbsolutePath().toString() + "\\File Server\\task\\src\\server\\data\\";
    private static final String folderPath = "C:\\Users\\Dmitriy\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data\\";
    private static IdToNameMapper fileNames;
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");

    private static String createFullPath(String fileName) {
        return folderPath + fileName;
    }

    public static void initStorage() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(folderPath + "id2map.dat"))) {
            fileNames = (IdToNameMapper) ois.readObject();
        } catch (Exception ex) {
            System.out.println("Id2Map corrupted");
            fileNames = new IdToNameMapper();
        }
    }

    public static void saveFileNamesInfo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(folderPath + "id2map.dat"))) {
            oos.writeObject(fileNames);
        } catch (Exception ex) {
            System.out.print("Can't save file names info. All data will be lost");
        }
    }


    ///Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    public RequestExecuter(DataInputStream input, DataOutputStream output) {
        ///####this.socket = sock;
        this.input = input;
        this.output = output;
    }

    public static boolean isFileExists(FileDescription file) {
        return fileNames.isExist(file);
    }

    public void executeRequest(FileDescription fileDescr, CheckedConsumer<FileDescription> func) {
        try {
            func.accept(fileDescr);
        } catch (IOException ex) {
            System.out.println("Socket exception occur");
        } finally {

            try {
                input.close();
                output.close();
                // here socket closes automatically
            } catch (IOException e) {
                // I don't know what to do if this event is happened
            }
        }
    }

    public void put(FileDescription fileDescr) throws IOException {
        //check if file exists
        ServerRespond response = new ServerRespond(ServerCode.FILE_EXIST);
        int id = 0;

        if (fileDescr.getName().isEmpty()) {
            do {
                fileDescr.setFileName(dtf.format(LocalDateTime.now()));
                // not so beautiful as I want
            } while (isFileExists(fileDescr));
        } else if (isFileExists(fileDescr)) {
            output.writeUTF(response.toString());
            return;
        }

        ClientServerTasks rxTask = new ClientServerTasks();
        File file = new File(createFullPath(fileDescr.getName()));
        //debug
        System.out.println("Create File: " + createFullPath(fileDescr.getName()));

        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            if (rxTask.receiveFile(fileOut, input)) {
                id = fileNames.add(fileDescr.getName());
                response.setCode(ServerCode.OK_CODE);
                response.setAdditionalInfo(Integer.toString(id));
            } else {
                response.setCode(ServerCode.ERR_CODE);
            }
            output.writeUTF(response.toString());
        } catch (FileNotFoundException ex) {
            System.out.println("Server side: File not found exception occur!");
            System.out.println("File path: " + createFullPath(fileDescr.getName()));
        }
    }

    public void delete(FileDescription fileDescr) throws IOException {
        ServerRespond respond = new ServerRespond(ServerCode.ERR_CODE);

        if (isFileExists(fileDescr)) {
            //debug
            System.out.println("Server DELETE request:file successfully found in internal storage");
            if (fileDescr.isFileAsID()) {
                fileDescr.setFileName(fileNames.getFileNameFromId(fileDescr.getId()));
            }

            //debug
            System.out.println("server:Delete file Path: " + createFullPath(fileDescr.getName()));


            File file = new File(createFullPath(fileDescr.getName()));

            System.out.println("Server file exists?: " + (file.exists() ? "true" : "false"));
            if (file.delete()) {
                fileNames.remove(fileDescr);
                respond.setCode(ServerCode.OK_CODE);
                //output.writeUTF(respond.toString());
            }
        } else {
            System.out.println("Server DELETE request: can't find file in internal storage");
        }
            //respond.setAdditionalInfo(createFullPath(fileDescr.getName()) + "not found in internal storage");
        output.writeUTF(respond.toString());


    }

    public void get(FileDescription fileDescr) throws IOException {
        ServerRespond respond = new ServerRespond(ServerCode.ERR_CODE);

        boolean fileFound = isFileExists(fileDescr);
        if (fileFound) {
            respond.setCode(ServerCode.OK_CODE);
            if (fileDescr.isFileAsID()) {
                fileDescr.setFileName(fileNames.getFileNameFromId(fileDescr.getId()));
            }
        }
        output.writeUTF(respond.toString());
        if (fileFound) {
            ClientServerTasks txTask = new ClientServerTasks();
            FileInputStream rdFile = new FileInputStream(new File(createFullPath(fileDescr.getName())));
            txTask.transmitFile(rdFile, output);
        }

    }
}
