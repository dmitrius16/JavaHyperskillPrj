package readability;

import java.io.*;
import java.util.Set;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No file");
        } else if (args.length == 1) {
            if (!args[0].equals("")) {
                File file = new File(args[0]);
                try (BufferedReader inp = new BufferedReader(new FileReader(file));) {
                    String resStr = "";
                    String temp;
                    while ((temp = inp.readLine()) != null) {
                        resStr = String.join("", temp);
                    }
                    TextMetrics textMetr = new TextMetrics();
                    textMetr.setText(resStr);
                    textMetr.printTextParameters();
                    String method = chooseCalcMethod(textMetr);
                    System.out.println(textMetr.getScoreResults(method));
                } catch (IOException ex) {
                    System.out.println("Cannot read file: " + args[0]);
                }
            }
        }
    }

    public static String chooseCalcMethod(TextMetrics txtMetrics) throws IOException {
        Scanner usrInp = new Scanner(System.in);
        System.out.print("Enter the score you want to calculate (" + TextMetrics.getSupportedCalcMethods() + "): ");
        String res = usrInp.nextLine();
        System.out.println();
        return res;
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
    private Pattern cntVowels = Pattern.compile("[aeiouy]", Pattern.CASE_INSENSITIVE);
    private Pattern cntDblVowels = Pattern.compile("[aeiouy]{2}", Pattern.CASE_INSENSITIVE);
    private String text;

    private static String calcMethodsNames[] = {"ARI", "FK", "SMOG", "CL"};
    private Map<String, BaseReadabilityScore> scoreMethods = Map.of(calcMethodsNames[0], new AutomatedReadabilityScore(),
            calcMethodsNames[1], new FleschKincaidScore(),
            calcMethodsNames[2], new SMOGScore(),
            calcMethodsNames[3], new ColemanLiauScore());

    public static String getSupportedCalcMethods() {
        String res = String.join(", ", calcMethodsNames);
        res += ", all";
        return res;
    }

    public TextMetrics() {
    }

    public int getCntCharacters() {
        return this.characters;
    }

    public int getCntWords() {
        return this.words;
    }

    public int getCntSentences() {
        return this.sentences;
    }

    public int getCntSyllables() {
        return this.syllables;
    }

    public int getCntPolySyllables() {
        return this.polysyllables;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
        calcWords();
        calcSentences();
        for (Map.Entry<String, BaseReadabilityScore> el : scoreMethods.entrySet()) {
            BaseReadabilityScore method = el.getValue();
            method.calcScore(this);
        }
    }

    public String getScoreResults(String methodName) {
        Set<String> calcMethods = scoreMethods.keySet();
        final String formatStr = "%s: %.2f (about %s year olds).\n";
        String res = "";
        if (calcMethods.contains(methodName)) {
            BaseReadabilityScore method = scoreMethods.get(methodName);
            res = String.format(formatStr, method.getName(), method.getScore(), method.getAgeUpperBound().toString());
        } else if (methodName.equals("all")) {
            StringBuilder resultStr = new StringBuilder();
            int sumAges = 0;
            for (String mName : calcMethodsNames) {
                BaseReadabilityScore method = scoreMethods.get(mName);
                resultStr.append(String.format(formatStr, method.getName(), method.getScore(), method.getAgeUpperBound().toString()));
                sumAges += method.getAgeUpperBound();
            }
            resultStr.append(String.format("\nThis text should be understood in average by %.2f year olds.", (double) sumAges / 4.));
            res = resultStr.toString();
        } else
            System.out.println("Unsupported calc method");
        return res;
    }

    public void printTextParameters() {
        System.out.println("The text is:");
        System.out.print(text);
        System.out.println();
        System.out.printf("Words: %d\n", words);
        System.out.printf("Sentences: %d\n", sentences);
        System.out.printf("Characters: %d\n", characters);
        System.out.printf("Syllables: %d\n", syllables);
        System.out.printf("Polysyllables: %d\n", polysyllables);
    }

    private int calcWords() {
        String[] words = this.text.split("[\\s+]");
        int cnt = 0;

        for (String word : words) {
            if (!word.isEmpty()) {
                cnt++;
                int cntSyllables = calcSyllables(word);

                syllables += cntSyllables;
                if (cntSyllables > 2)
                    polysyllables++;
            }
        }
        this.words = cnt;
        calcCharacters(words);
        return this.words;
    }

    private int calcSentences() {
        String[] sentences = this.text.split("[.?!]");
        this.sentences = sentences.length;
        return this.sentences;
    }

    private int calcCharacters(String[] words) {
        int cnt = 0;
        for (String el : words) {
            cnt += el.length();
        }
        this.characters = cnt;
        return cnt;
    }

    private int calcSyllables(String word) {
        word = word.replaceAll("[.,?!]", "").toLowerCase();
        Matcher m = cntVowels.matcher(word);
        int numSyllables = 0;
        while (m.find()) {
            numSyllables++;
        }
        m = cntDblVowels.matcher(word);
        //exclude double vowels
        while (m.find()) {
            numSyllables--;
        }
        //exclude last e
        if (word.charAt(word.length() - 1) == 'e') {
            int ind = word.length() - 2;
            if (!m.find(ind))    //except last two syllables
                numSyllables--;
        }
        if (numSyllables == 0) {
            if (!word.matches("[0-9]*"))
                numSyllables = 1;
        }
        return numSyllables;
    }
}

abstract class BaseReadabilityScore {
    private String name;
    protected double score;

    public BaseReadabilityScore(String name) {
        this.name = name;
        this.score = 0.;
    }

    public abstract void calcScore(TextMetrics textStat);

    public String getName() {
        return this.name;
    }

    public double getScore() {
        return this.score;
    }

    public Integer getAgeUpperBound() {
        String strRes = getAge();
        int indUpper = strRes.indexOf('-');
        if (indUpper != -1)
            return Integer.parseInt(strRes.substring(indUpper + 1));
        if (strRes.equals("24+"))
            return 24;
        return -1;
    }

    public String getAge() {
        int intScore = (int) (Math.round(score));
        String temp;
        switch (intScore) {
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
            default:
                if (intScore >= 14) {
                    temp = "24+";
                } else {
                    temp = "Undefined age";
                }
                break;
        }
        return temp;
    }
}

class AutomatedReadabilityScore extends BaseReadabilityScore {
    public AutomatedReadabilityScore() {
        super("Automated Readability Index");
    }

    @Override
    public void calcScore(TextMetrics textStat) {
        double res = 4.71 * ((double) textStat.getCntCharacters() / (double) textStat.getCntWords()) +
                0.5 * ((double) textStat.getCntWords() / (double) textStat.getCntSentences()) - 21.43;
        score = res;
        ///return res;
    }
}

class FleschKincaidScore extends BaseReadabilityScore {
    public FleschKincaidScore() {
        super("Flesch-Kincaid readability tests");
    }

    @Override
    public void calcScore(TextMetrics textStat) {
        double res = 0.39 * textStat.getCntWords() / textStat.getCntSentences() +
                11.8 * textStat.getCntSyllables() / textStat.getCntWords() - 15.59;
        score = res;
        ///return res;
    }
}

class SMOGScore extends BaseReadabilityScore {
    public SMOGScore() {
        super("Simple Measure of Gobbledygook");
    }

    @Override
    public void calcScore(TextMetrics textStat) {
        double res = 1.043 * Math.sqrt(30. * textStat.getCntPolySyllables() / textStat.getCntSentences()) + 3.1291;
        score = res;
        ///return res;
    }
}

class ColemanLiauScore extends BaseReadabilityScore {
    private double getAverCharsPer100Words(TextMetrics textStat) {
        int chars = textStat.getCntCharacters();
        int words = textStat.getCntWords();

        return 100. * chars / words;
    }

    private double getAverSentencePer100Words(TextMetrics textStat) {
        int sentences = textStat.getCntSentences();
        int words = textStat.getCntWords();
        return 100. * sentences / words;
    }

    public ColemanLiauScore() {
        super("Coleman-Liau index");
    }

    @Override
    public void calcScore(TextMetrics textStat) {
        double L = getAverCharsPer100Words(textStat);
        double S = getAverSentencePer100Words(textStat);
        double res = 0.0588 * L - 0.296 * S - 15.8;
        score = res;
        ///return res;
    }
}


