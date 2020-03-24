package readability;
import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No file");
        } else if (args.length == 1) {
            if(!args[0].equals("")) {
                File file = new File(args[0]);
                try (BufferedReader inp = new BufferedReader(new FileReader(file));) {
                    String resStr = "";
                    String temp;
                    while((temp = inp.readLine()) != null) {
                        resStr = String.join("",temp);
                    }
                    TextMetrics textMetr = new TextMetrics(resStr);
                    textMetr.printTextParameters();


                } catch(IOException ex) {
                    System.out.println("Cannot read file: " + args[0]);
                }
            }
        }
    }

    public static void chooseCalcMethod(TextMetrics txtMetrics) {
        System.out.println("Enter the score you want to calculate (" + TextMetrics.getSupportedCalcMethods() + "):");

    }

    public static String handleResult(int averLen) {
        return averLen > 10 ? "HARD" : "EASY";
    }
}

class TextMetrics {
    private int characters;
    private int words;
    private int sentences;
    private double score;
    private String text;
    private static BaseReadabilityScore[] scoreMethodicks = {new AutomatedReadabilityScore(),
                                                             new FleschKincaidScore(),
                                                             new SMOGScore(),
                                                             new ColemanLiauScore()};
    private static String calcMethods = "ARI, FK, SMOG, CL, all";
    public TextMetrics(String text) {
        this.text = text;
        calcWords(text);
        calcSentences(text);
        score = CalcScore();
    }
    /*
    public int getCntCharacters(){return this.characters;}
    public  int getCntWords(){return this.words;}
    public int getCntSentences(){return this.sentences;}
    public double getScore(){return this.score;}
    public String getText(){return this.text;}
    */
    public static String getSupportedCalcMethods() {return calcMethods;};
    public void printTextParameters() {
        System.out.println("The text is:");
        System.out.print(text);
        System.out.println();
        System.out.printf("Words: %d\n",words);
        System.out.printf("Sentences: %d\n",sentences);
        System.out.printf("Characters: %d\n", characters);
        //TODO
        // print Syllables
        // print Pulysyllables
    }

    private int calcWords(String text) {
        String[] words = text.split("[\\s+]");
        int cnt = 0;
        for(String el : words) {
            if(!el.isEmpty()) {
                cnt++;
            }
        }
        this.words = cnt;
        calcCharacters(words);
        return this.words;
    }

    private int calcSentences(String text) {
        String[] sentences = text.split("[.?!]");
        this.sentences = sentences.length;
        return this.sentences;

    }

    private int calcCharacters(String[] words) {
        int cnt = 0;
        for(String el : words) {
            cnt += el.length();
        }
        this.characters = cnt;
        return cnt;
    }

    private double CalcScore() {
        double result = 4.71 * ((double)this.characters / (double)this.words) +
                0.5 * ((double)this.words/(double)this.sentences) - 21.43;
        return result;
    }
}

abstract class BaseReadabilityScore {
    private String name;
    private double score;
    public BaseReadabilityScore(String name) {
        this.name = name;
        this.score = 0.;
    }
    public abstract double calcScore();
    String getName(){return this.name;}
    public String getAge() {
        int intScore = (int)(Math.ceil(score));
        String temp;
        switch(intScore) {
            case 1:
                temp = "5-6";
                break;
            case 2:
                temp = "6-7";
                break;
            case 3:
                temp = "7-9";
                break;
            case 4:
                temp = "9-10";
                break;
            case 5:
                temp = "10-11";
                break;
            case 6:
                temp = "11-12";
                break;
            case 7:
                temp = "12-13";
                break;
            case 8:
                temp = "13-14";
                break;
            case 9:
                temp = "14-15";
                break;
            case 10:
                temp = "15-16";
                break;
            case 11:
                temp = "16-17";
                break;
            case 12:
                temp = "17-18";
                break;
            case 13:
                temp = "18-24";
                break;
            case 14:
                temp = "24+";
                break;
            default:
                temp = "Undefined age";
                break;
        }
        return temp;
    }
}

class AutomatedReadabilityScore extends BaseReadabilityScore {
    public AutomatedReadabilityScore() {
        super("Automated readability index");
    }
    @Override
    public double calcScore() {
        return 0.0;
    }
}

class FleschKincaidScore extends  BaseReadabilityScore {
    public FleschKincaidScore() {
        super("Flesch-Kincaid readability tests");
    }

    @Override
    public double calcScore() {
        return 0;
    }
}

class SMOGScore extends BaseReadabilityScore {
    public SMOGScore() {
        super("SMOG index");
    }

    @Override
    public double calcScore() {
        return 0;
    }
}

class ColemanLiauScore extends BaseReadabilityScore {
    public ColemanLiauScore() {
        super("Coleman-Liau index");
    }

    @Override
    public double calcScore() {
        return 0;
    }
}


