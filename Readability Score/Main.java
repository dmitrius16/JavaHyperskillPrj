package readability;
import java.io.*;
import java.util.Set;
import java.util.Map;
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
                    TextMetrics textMetr = new TextMetrics();
                    textMetr.setText(resStr);
                    textMetr.printTextParameters();
                    String method = chooseCalcMethod(inp,textMetr);
                    System.out.println(textMetr.getScoreResults(method));
                } catch(IOException ex) {
                    System.out.println("Cannot read file: " + args[0]);
                }
            }
        }
    }

    public static String chooseCalcMethod(BufferedReader input, TextMetrics txtMetrics) throws IOException {
        System.out.println("Enter the score you want to calculate (" + TextMetrics.getSupportedCalcMethods() + "):");
        String usrInp = input.readLine();
        return usrInp;
    }

    public static String handleResult(int averLen) {
        return averLen > 10 ? "HARD" : "EASY";
    }
}

class TextMetrics {
    private int characters;
    private int words;
    private int sentences;
    private int syllables;
    private int polysyllables;
 ///###   private double score;
    private String text;
 ///###   private String curCalcMethod;
    Map<String, BaseReadabilityScore> scoreMethodics = Map.of("ARI",new AutomatedReadabilityScore(),
                                                        "FK", new FleschKincaidScore(),
                                                        "SMOG", new SMOGScore(),
                                                        "CL", new ColemanLiauScore());
    private static String calcMethods = "ARI, FK, SMOG, CL, all";

    public static String getSupportedCalcMethods() { return calcMethods;}

    public TextMetrics() {}

    public int getCntCharacters(){return this.characters;}
    public  int getCntWords(){return this.words;}
    public int getCntSentences(){return this.sentences;}
    public int getCntSyllables(){return this.syllables;}
    public int getCntPolySyllables(){return this.polysyllables;}
 ///###   public double getScore(){return this.score;}
    public String getText(){return this.text;}

    public void setText(String text) {
        this.text = text;
        for (Map.Entry<String, BaseReadabilityScore> el : scoreMethodics.entrySet()) {
            BaseReadabilityScore method = el.getValue();
            method.calcScore(this);
        }
    }

    public String getScoreResults(String methodName) {
        Set<String> calcMethods = scoreMethodics.keySet();
        String res="";
        if(calcMethods.contains(methodName)) {
            BaseReadabilityScore method = scoreMethodics.get(methodName);
            res = String.format("%s: %.2f (about %s year olds).",method.getName(),method.getScore(),method.getAgeUpperBound() );
        } else if (methodName.equals("all")) {

        } else
            System.out.println("Unsuppoorted calc mehod");
        return res;
    }

    public void printTextParameters() {
        System.out.println("The text is:");
        System.out.print(text);
        System.out.println();
        System.out.printf("Words: %d\n",words);
        System.out.printf("Sentences: %d\n",sentences);
        System.out.printf("Characters: %d\n", characters);
        //TODO
        // print Syllables
        // print Polysyllables
    }

  //  public

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
/*
    private double CalcScore() {
        double result = 4.71 * ((double)this.characters / (double)this.words) +
                0.5 * ((double)this.words / (double)this.sentences) - 21.43;
        return result;
    }
 */

}

abstract class BaseReadabilityScore {
    private String name;
    protected double score;
    public BaseReadabilityScore(String name) {
        this.name = name;
        this.score = 0.;
    }
    public abstract double calcScore(TextMetrics textStat); // TODO make return type void
    public String getName() {return this.name;}
    public double getScore() {return this.score;}
    public String getAgeUpperBound() {
        String res = getAge();
        int indUpper = res.indexOf('-');
        if(indUpper != -1)
            return res.substring(indUpper + 1);
        return res;
    }
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
    public double calcScore(TextMetrics textStat) {
        double res = 4.71 * ((double)textStat.getCntCharacters() / (double)textStat.getCntWords()) +
                0.5 * ((double)textStat.getCntWords() / (double)textStat.getCntSentences()) - 21.43;
        score = res;
        return res;
    }
}

class FleschKincaidScore extends  BaseReadabilityScore {
    public FleschKincaidScore() {
        super("Flesch-Kincaid readability tests");
    }

    @Override
    public double calcScore(TextMetrics textStat) {
        double res = 0.39 * textStat.getCntWords() / textStat.getCntSentences() +
                    11.8 * textStat.getCntSyllables() / textStat.getCntWords() - 15.59;
        score = res;
        return res;
    }
}

class SMOGScore extends BaseReadabilityScore {
    public SMOGScore() {
        super("SMOG index");
    }

    @Override
    public double calcScore(TextMetrics textStat) {
        double res = 1.043 * Math.sqrt(30. * textStat.getCntPolySyllables() / textStat.getCntSentences()) + 3.1291;
        score = res;
        return res;
    }
}

class ColemanLiauScore extends BaseReadabilityScore {
    private double getAverCharsPer100Words(TextMetrics textStat) {
        return 0.0;
    }

    private double getAverSentencePer100Words(TextMetrics textStat) {
        return 0.0;
    }

    public ColemanLiauScore() {
        super("Coleman-Liau index");
    }

    @Override
    public double calcScore(TextMetrics textStat) {
        double L = getAverCharsPer100Words(textStat);
        double S = getAverSentencePer100Words(textStat);
        double res = 0.0588 * L - 0.296 * S - 15.8;
        return res;
    }
}


