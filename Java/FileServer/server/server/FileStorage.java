package server;

import java.io.*;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


import common.ClientServerTasks;
import common.ClientServerTasks.ServerRespond;
import common.ClientServerTasks.ServerCode;
import common.FileDescription;

public class FileStorage {
    private final String folderPath = Paths.get("").toAbsolutePath().toString() + "\\File Server\\task\\src\\server\\data\\";
    private IdToNameMapper fileNames;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");

    private String createFullPath(String fileName) {
        return folderPath + "\\" + fileName;
    }

    public FileStorage() {
        ///File folder = new File(folderPath);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(folderPath + "id2map.dat"))) {
            fileNames = (IdToNameMapper) ois.readObject();
        } catch (Exception ex) {
            System.out.println("Id2Map corrupted");
            fileNames = new IdToNameMapper();
        }
    }

    public boolean isFileExists(FileDescription file) {
        return fileNames.isExist(file);
    }

    public void saveFileNamesInfo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(folderPath + "id2map.dat"))) {
            oos.writeObject(fileNames);
        } catch (Exception ex) {
            System.out.print("Can't save file names info. All data will be lost");
        }
    }

    public void put(FileDescription fileDescr, DataInputStream input, DataOutputStream output) {
        //check if file exists
        ServerRespond response = new ServerRespond(ServerCode.FILE_EXIST);
        int id = 0;

        if (fileDescr.getName().isEmpty()) {
            do {
                fileDescr.setFileName(dtf.format(LocalDateTime.now()));
                // not so beautiful as I want
            } while (isFileExists(fileDescr));
        } else if (isFileExists(fileDescr)) {
            try {
                output.writeUTF(response.toString());
            } catch (IOException ex) {
                System.out.println("Socket exception occur! Can't send response to client");
                return;
            }
        }

        ClientServerTasks rxTask = new ClientServerTasks();
        File file = new File(createFullPath(fileDescr.getName()));
        try (FileOutputStream fileOut = new FileOutputStream(file)) {
            if (rxTask.receiveFile(fileOut, input)) {
                id = fileNames.add(fileDescr.getName());
                response.setCode(ServerCode.OK_CODE);
                response.setAdditionalInfo(Integer.toString(id));
            } else
                response.setCode(ServerCode.ERR_CODE);

            output.writeUTF(response.toString());

        } catch (FileNotFoundException ex) {
            System.out.println("File not found exception occur!");
        } catch (IOException ex) {
            System.out.println("Socket exception occur! Receive file and send response");
        }
    }

    public void delete(FileDescription fileDescr, DataOutputStream output) {
        ServerRespond respond = new ServerRespond(ServerCode.ERR_CODE);
        try {
            if (isFileExists(fileDescr)) {
                if (fileDescr.isFileAsID()) {
                    fileDescr.setFileName(getFileNameFromId(fileDescr.getId()));
                }

                File file = new File(createFullPath(fileDescr.getName()));
                if (file.exists() && file.delete()) {
                    fileNames.remove(fileDescr);
                    respond.setCode(ServerCode.OK_CODE);
                }
            }
            output.writeUTF(respond.toString());
        } catch (IOException ex) {
            System.out.println("Exception occur during delete request");
        }
    }

    public void get(FileDescription fileDescr, DataOutputStream output) {
        ServerRespond respond = new ServerRespond(ServerCode.ERR_CODE);
        try {
            boolean fileFound = isFileExists(fileDescr);
            if (fileFound) {
                respond.setCode(ServerCode.OK_CODE);
                if (fileDescr.isFileAsID()) {
                    fileDescr.setFileName(getFileNameFromId(fileDescr.getId()));
                }
            }
            output.writeUTF(respond.toString());
            if (fileFound) {
                ClientServerTasks txTask = new ClientServerTasks();
                FileInputStream rdFile = new FileInputStream(new File(createFullPath(fileDescr.getName())));
                txTask.transmitFile(rdFile, output);
            }
        } catch (IOException | OutOfMemoryError ex) {
            ex.printStackTrace();
        }
    }


    public String getFileNameFromId(int id) {
        return fileNames.getFileNameFromId(id);
    }

    public int getFileIdFromName(String name) {
        return fileNames.getIdFromFileName(name);
    }
}
