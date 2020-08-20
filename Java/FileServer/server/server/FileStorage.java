package server;
import java.io.IOException;
import java.util.List;
import java.util.LinkedList;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileStorage {
    private final String folderPath = ".\\server\\data";
    private List<String> fileNames = new LinkedList<>();
    private String createFullPath(String fileName) {
        return folderPath + fileName;
    }
    public FileStorage()
    {
        File folder = new File(System.getProperty("user.dir"));
        File[] lstFiles = folder.listFiles();
        for(File file : lstFiles) {
            if (file.isFile())
                fileNames.add(file.getName());
        }
    }

    public boolean isFileExists(String fileName) {
        return fileNames.contains(fileName);
    }

    boolean put(String fileName, String content) {
        //check if file exists
        boolean result = false;
        if (isFileExists(fileName) == false) {
            String fullFileName = createFullPath(fileName);
            fileNames.add(fileName);
            try (FileWriter fileWriter = new FileWriter(new File(fullFileName))) {
                fileWriter.write(content);
                result = true;
            }catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    boolean delete(String fileName) {
        boolean result = false;
        if (isFileExists(fileName)) {
            File file = new File(createFullPath(fileName));
            result = file.delete();
            if (result) {
                fileNames.remove(fileName);
            }
        }
        return result;
    }

    boolean get(String fileName, FileContent content){
        boolean result = false;
        if (isFileExists(fileName)) {
            try {
                content.setContent(Files.readAllBytes(Paths.get(fileName)));
                result = true;
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

}
