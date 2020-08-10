package server;
import java.util.LinkedList;
import java.util.List;
public class FileStorageSimulator {
    private List<String> fileNamesStorage = new LinkedList<>();
    private int numFiles;


    public FileStorageSimulator() {
        numFiles = 0;
    }

    private boolean isNoFile(String fileName) {
        return fileNamesStorage.indexOf(fileName) == -1;
    }

    private StringBuilder fileOpOkStr(String fileName) {
        return new StringBuilder("The file ").append(fileName).append(" was ");
    }

    private StringBuilder fileOpFaultStr(String fileName) {
        return new StringBuilder("The file ").append(fileName).append(" not found");
    }

    private String fileAddedOkStr(String fileName) {
        StringBuilder sb = new StringBuilder("The file ").append(fileName).append(" added successfully");
        return sb.toString();
    }

    private String fileAddedFaultStr(String fileName) {
        StringBuilder sb = new StringBuilder("Cannot add the file ").append(fileName);
        return sb.toString();
    }

    public String add(String fileName) {
        String res;
        try {
            int num = Integer.parseInt(fileName, 4, fileName.length(), 10);
            if (isNoFile(fileName) && (num >= 1 && num <= 10)) {
                fileNamesStorage.add(fileName);
                numFiles += 1;
                res = fileAddedOkStr(fileName);
            } else {
                res = fileAddedFaultStr(fileName);
            }
        } catch (NumberFormatException ex) {
            res = fileAddedFaultStr(fileName);
        }
        return res;
    }

    public String get(String fileName) {

        StringBuilder resStr;
        if (isNoFile(fileName)) {
            resStr = fileOpFaultStr(fileName);
        } else {
            resStr = fileOpOkStr(fileName).append("sent");
        }
        return resStr.toString();
    }

    public String delete(String fileName) {
        StringBuilder resStr;
        if (isNoFile(fileName)) {
            resStr = fileOpFaultStr(fileName);
        } else {
            fileNamesStorage.remove(fileName);
            resStr = fileOpOkStr(fileName).append("deleted");
        }
        return resStr.toString();
    }
}
