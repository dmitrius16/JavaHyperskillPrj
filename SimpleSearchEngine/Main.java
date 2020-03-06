package search;
import javax.print.DocFlavor;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;
import java.util.LinkedList;
public class Main {
    static int numItems;
    static String[] records;
    static Scanner scanner;
    static List<String> fndLst;

    static void query() {


            System.out.println("Enter a name or email to search all suitable people.");
            String temp = scanner.nextLine();
            fndLst.clear();
            search(temp);
            if (fndLst.isEmpty()) {
                System.out.println("No matching people found.");
            } else {
                System.out.println("Found people:");
                fndLst.forEach(el -> System.out.println(el));
            }

    }

    public static void search(String srchStr) {
        Pattern pattern = Pattern.compile(srchStr, Pattern.CASE_INSENSITIVE);
        for (String str : records) {
            Matcher matcher = pattern.matcher(str);
            if (matcher.find()) {
                fndLst.add(str);
            }
        }
    }
    public static void userInput() {
        System.out.println("Enter the number of people:");
        numItems = Integer.parseInt(scanner.nextLine());
        records = new String[numItems];
        System.out.println("Enter all people:");
        for (int i = 0; i < numItems; i++)
            records[i] = scanner.nextLine();
    }
    public static void showMenuItems() {
        String[] items = {"Find a person", "Print all people", "Exit"};
        String[] itemsNum = {"1. ", "2. ", "0. "};
        System.out.println("=== Menu ===");
        for (int i = 0; i < items.length; i++) {
            System.out.println(itemsNum[i] + items[i]);
        }
        System.out.println("");
    }

    public static void menuHandler() {
        boolean exit = false;
        while (!exit) {
            showMenuItems();
            int userChoice = Integer.parseInt(scanner.nextLine());
            switch (userChoice) {
                case 1:
                    query();
                    break;
                case 2:
                    for (String el : records) {
                        System.out.println(el);
                    }
                    break;
                case 0:
                    System.out.println("Bye!");
                    exit = true;
                    break;
                default:
                    System.out.println("Incorrect option! Try again.");
                    break;
            }
        }
    }
    public static void main(String[] args) {
        fndLst = new LinkedList<>();
        scanner = new Scanner(System.in);
        userInput();
        menuHandler();
    }
}