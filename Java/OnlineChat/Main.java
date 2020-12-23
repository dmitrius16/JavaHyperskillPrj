package chat;
import java.util.Scanner;
public class Main {

    private final String[] technicalText = {"joined", "sent",};

    public enum ServiceWords {
        JOINED("joined"),
        CONNECTED("connected"),
        DISCONNECTED("disconnected"),
        SENT("sent"),
        LEFT("left");

        private String repr;
        private ServiceWords(String repr){this.repr = repr;}
        public String getRepr(){return repr;}
    }

    public static String filterMessage(String input) {
        String[] partText = input.split("\\s+", 3);

        if (partText[1].equals(ServiceWords.SENT.repr)) {
            StringBuilder sb = new StringBuilder().append(partText[0])
                                                .append(": ")
                                                .append(partText[2]);
            return sb.toString();
        }

        return "";
    }

    public static void main(String[] args) {
        while(true) {
            Scanner scanner = new Scanner(System.in);
            String usrInp = scanner.nextLine();
            if(usrInp.equals("exit")) {
                break;
            }
            System.out.println(filterMessage(usrInp));
        }
    }
}
