package server;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import common.ClientServer;
import common.ClientServer.ServerRespond;
import common.ClientServer.ServerCode;

public class FileStorage {
    private final String folderPath = "C:\\Users\\sysoevd\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data\\";
    private IdToNameMapper fileNames;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
    private String createFullPath(String fileName) {
        return folderPath + "\\" + fileName;
    }
    public FileStorage() {
        File folder = new File(folderPath);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("id2map.dat"))) {
            fileNames = (IdToNameMapper)ois.readObject();
        } catch(Exception ex) {
            System.out.println("Id2Map corrupted");
            fileNames = new IdToNameMapper();
        }
    }

    public boolean isFileExists(String fileName)
    {
        return fileNames.isExist(fileName);
    }

    public boolean isFileExists(int id) {
        return fileNames.isExist(id);
    }

    public void saveFileNamesInfo() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("id2map.dat"))) {
            oos.writeObject(fileNames);
        } catch (Exception ex) {
            System.out.print("Can't save file names info. All data will be lost");
        }
    }

    public ServerRespond put(String fileName, DataInputStream input) {
        //check if file exists
        ServerCode result = ServerCode.FILE_EXIST;
        int id = 0;
        if (fileName.isEmpty()) {
            fileName = dtf.format(LocalDateTime.now());
        }

        if (isFileExists(fileName))
            return new ServerRespond(result);

        String fullFileName = createFullPath(fileName);
        id = fileNames.add(fileName);

        if (id != 0) {
            result = ClientServer.receiveFile(fullFileName, input) ? ServerCode.OK_CODE : ServerCode.ERR_CODE;
        }
        return new ServerRespond(result, id);
    }

    public ServerRespond delete(String fileName) {
        ServerCode result = ServerCode.ERR_CODE;
        if (isFileExists(fileName)) {
            File file = new File(createFullPath(fileName));
            if (file.delete()) {
                fileNames.remove(fileName);
                result = ServerCode.OK_CODE;
            }
        }
        return new ServerRespond(result);
    }

    public ServerRespond get(String fileName, FileContent content) {
        ServerCode result = ServerCode.ERR_CODE;
        if (isFileExists(fileName)) {
            try {
                content.setContent(Files.readAllBytes(Paths.get(createFullPath(fileName))));
                result = ServerCode.OK_CODE;
            } catch (IOException | OutOfMemoryError ex) {
                ex.printStackTrace();
            }
        }
        return new ServerRespond(result);
    }

    public ServerRespond get(int id, FileContent content) {
        return get(fileNames.getFileNameFromId(id), content);
    }

    public String getFileNameFromId(int id) {
        return fileNames.getFileNameFromId(id);
    }

    public int getFileIdFromName(String name) {
        return fileNames.getIdFromFileName(name);
    }
/*
    public static class FileContent {
        private String content = "";

        public void setContent(String content) {
            this.content = content;
        }

        public void setContent(byte[] cont) {
            String contStr = new String(cont);
            content = contStr;
        }

        public String getContent() {
            return this.content;
        }
    }
 */
}
