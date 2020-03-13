package search;
import java.io.IOException;
import java.util.*;
import java.io.File;
public class Main {
    ///static int numItems;
    static List<String> records;
    static Scanner scanner;
    static Map<String, List<Integer>> invIndex;
    static void query() {


            System.out.println("Enter a name or email to search all suitable people.");
            String temp = scanner.nextLine().toLowerCase();
            if (!invIndex.containsKey(temp)) {
                System.out.println("No matching people found.");
            } else {
                System.out.println("Found people:");
                List<Integer> lst = invIndex.get(temp);
                lst.forEach(el->System.out.println(records.get(el)));
                System.out.println("");
            }

    }

    public static void showMenuItems() {
        String[] items = {"Find a person", "Print all people", "Exit"};
        String[] itemsNum = {"1. ", "2. ", "0. "};
        System.out.println("=== Menu ===");
        for (int i = 0; i < items.length; i++) {
            System.out.println(itemsNum[i] + items[i]);
        }
       // System.out.println("");
    }

    public static void menuHandler() {
        boolean exit = false;
        while (!exit) {
            showMenuItems();
            int userChoice = Integer.parseInt(scanner.nextLine());
            switch (userChoice) {
                case 1:
                    //query();
                    SelectQueryStrategy();
                    break;
                case 2:
                    for (String el : records) {
                        System.out.println(el);
                    }
                    System.out.println("");
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

    public static void SelectQueryStrategy() {
        String[] namesStrategy = {"ALL","ANY","NONE"};
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String usrInp = scanner.nextLine();
        switch(usrInp) {
            case "ALL":
                break;
            case "ANY":
                break;
            case "NONE":
                break;
            default:
                System.out.println("Unsupported strategy");
                break;
        }

    }

    public static void buildInvIndex(String curString,int numStr) {
        String[] words = curString.toLowerCase().split("\\s+");
        for (String el : words) {
            if (invIndex.containsKey(el) ) {
                List<Integer> lst = invIndex.get(el);
                lst.add(numStr);
            } else {
                List<Integer> lst = new ArrayList<>();
                lst.add(numStr);
                invIndex.put(el,lst);
            }
        }
    }

    public static void main(String[] args) {

// process command line
        if (args.length == 0) {
            System.out.println("No file");
        } else if (args.length == 2) {
            if(args[0].equals("--data")) {
                File file = new File(args[1]);
                scanner = new Scanner(System.in);
                invIndex = new HashMap<String, List<Integer>>();
                try ( Scanner fileInp = new Scanner(file)) {
                    records = new ArrayList<>();
                    int numStr = 0;
                    while(fileInp.hasNext()) {
                        String curString = fileInp.nextLine();
                        records.add(curString);
                        buildInvIndex(curString,numStr++);
                    }
                    menuHandler();
                } catch(IOException ex) {
                    System.out.println("Cannot read file: " + args[1]);
                }
            }
        }
    }
}

interface QueryStrategy {
    public String[] query(String[] param);
}

class QueryStrategyContext {
    private QueryStrategy queryStrategy;

    public void setQueryStrategy(QueryStrategy queryStrategy) {
        this.queryStrategy = queryStrategy;
    }

    String[] executeQuery(String queryStr) {
        String[] queryParam = queryStr.split("\\s+");
        return queryStrategy.query(queryParam);
    }
}

class QueryALL implements QueryStrategy {
    @Override
    public String[] query(String[] param) {
        if(Main.invIndex.containsKey(param[0])) {

        }

    }
}