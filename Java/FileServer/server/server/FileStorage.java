package server;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class FileStorage {
    private final String folderPath = "C:\\Users\\sysoevd\\IdeaProjects\\File Server\\File Server\\task\\src\\server\\data\\";
    private List<String> fileNames = new LinkedList<>();
    private String createFullPath(String fileName) {
        return folderPath + fileName;
    }
    public FileStorage() {
    //    System.out.println("Working directory: " + folderPath);
        File folder = new File(folderPath);
        if (folder.exists()) {
            File[] lstFiles = folder.listFiles();
            if (Objects.nonNull(lstFiles)) {
                for (File file : lstFiles) {
                    if (file.isFile())
                        fileNames.add(file.getName());
                }
            }
        } else {
            folder.mkdirs();
        }
    }

    public boolean isFileExists(String fileName) {
        return fileNames.contains(fileName);
    }

    ServerCode put(String fileName, String content) {
        //check if file exists
        ServerCode result = ServerCode.FILE_EXIST;
        if (isFileExists(fileName) == false) {
            String fullFileName = createFullPath(fileName);
            fileNames.add(fileName);
            try (FileWriter fileWriter = new FileWriter(new File(fullFileName))) {
                fileWriter.write(content);
                result = ServerCode.OK_CODE;
            }catch (IOException ex) {
                ex.printStackTrace();
                result = ServerCode.ERR_CODE;
            }
        }
        return result;
    }

    ServerCode delete(String fileName) {
        ServerCode result = ServerCode.ERR_CODE;
        if (isFileExists(fileName)) {
            File file = new File(createFullPath(fileName));
            if (file.delete()) {
                fileNames.remove(fileName);
                result = ServerCode.OK_CODE;
            }
        }
        return result;
    }

    ServerCode get(String fileName, FileContent content){
        ServerCode result = ServerCode.ERR_CODE;
        if (isFileExists(fileName)) {
            try {
                content.setContent(Files.readAllBytes(Paths.get(createFullPath(fileName))));
                result = ServerCode.OK_CODE;
            } catch (IOException | OutOfMemoryError ex) {
                ex.printStackTrace();
            }
        }
        return result;
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

    public enum ServerCode {
        OK_CODE("200"),
        FILE_EXIST("403"),
        ERR_CODE("404");

        private String repr;
        ServerCode(String strVal) {repr = strVal;}
        String getRepr() {return repr;}
    }

}
