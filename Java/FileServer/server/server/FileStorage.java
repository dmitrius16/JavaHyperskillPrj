package server;
import java.io.*;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;

enum ServerCode {
    OK_CODE("200"),
    FILE_EXIST("403"),
    ERR_CODE("404");

    private String repr;
    ServerCode(String strVal) {repr = strVal;}
    String getRepr() {return repr;}
}

class ServerRespond {
    private ServerCode code;
    private String additional_info;
    public ServerRespond(ServerCode code) {
        this.code = code;
        this.additional_info = "";
    }
    public ServerRespond(ServerCode code, int id) {
        this.code = code;
        this.additional_info = Integer.toString(id);
    }

    ServerCode getCode() {return code;}

    public String toString() {
        return code.getRepr() + " " + additional_info;
    }
}

public class FileStorage {
    private final String folderPath = "C:\\Users\\sysoevd\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data\\";
    private IdToNameMapper fileNames; 
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

    public ServerRespond put(String fileName, String content) {
        //check if file exists
        ServerCode result = ServerCode.FILE_EXIST;
        int id = 0;
        if (isFileExists(fileName) == false) {
            String fullFileName = createFullPath(fileName);
            id = fileNames.add(fileName);
            if (id != 0) {
                try (FileWriter fileWriter = new FileWriter(new File(fullFileName))) {
                    fileWriter.write(content);
                    result = ServerCode.OK_CODE;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    result = ServerCode.ERR_CODE;
                }
            }
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
}
