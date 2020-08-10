package server;
import java.lang.reflect.Array;
import java.util.Scanner;
public class Main {
    private static String[] items = {"add", "get", "delete", "exit"};
    public static void handleMenuItems(FileStorageSimulator fileStorage) {
        Scanner scanner = new Scanner(System.in);
        boolean exec = true;
        while (exec) {
            String[] usrInp = scanner.nextLine().split("\\s+");
            String res = "";
            switch (usrInp[0]) {
                case "add":
                    res = fileStorage.add(usrInp[1]);
                    break;
                case "get":
                    res = fileStorage.get(usrInp[1]);
                    break;
                case "delete":
                    res = fileStorage.delete(usrInp[1]);
                    break;
                case "exit":
                    exec = false;
                    break;
            }
            System.out.println(res);
        }
    }

    public static void main(String[] args) {
        FileStorageSimulator fileStorage = new FileStorageSimulator();
        handleMenuItems(fileStorage);
    }
}
