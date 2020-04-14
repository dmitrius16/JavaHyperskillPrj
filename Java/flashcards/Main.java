package flashcards;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        DeckMenu menu = new DeckMenu(new DeckFlashCards());
        menu.userAskingLoop();
///###        DeckFlashCards deck = new DeckFlashCards(new Scanner(System.in));
///###        deck.fill();
///###        deck.askQuestions();
    }

}

class DeckFlashCards
{
    LinkedHashMap<String,String> terms;
    /// Scanner input;

    DeckFlashCards()
    {
        terms = new LinkedHashMap<>();
        //this.input = inp;
    }
    boolean add(String term, String definition){
        if(terms.containsKey(term))
            return false;
        terms.put(term, definition);
        return true;
    }
    void replaceDefinition(String term,String newDefenition)
    {
        terms.put(term,newDefenition);
    }
    boolean isCardTermExists(String cardName)
    {
        return terms.containsKey(cardName);
    }

    boolean isCardDefExists(String cardDef)
    {
        boolean findDef = false;
        for(Map.Entry<String,String> entry : terms.entrySet())
        {
            String tempDef = entry.getValue();
            if(cardDef.equals(tempDef)){
                findDef = true;
                break;
            }
        }
        return findDef;
    }

    void removeCard(String cardTerm){
        terms.remove(cardTerm);
    }
    String getCardDefinitionFromTerm(String cardTerm)
    {
        if(terms.containsKey(cardTerm))
            return terms.get(cardTerm);
        return "???!!!???";
    }
    String getCardTermFromDefinition(String cardDef)
    {
        boolean findDef = false;
        String tempDef = null;
        String term = null;
        for(Map.Entry<String,String> entry : terms.entrySet())
        {
            tempDef = entry.getValue();
            if(cardDef.equals(tempDef)){
                findDef = true;
                term = entry.getKey();
                break;
            }
        }
        return findDef ? term : "???!!!???";
    }
    Set<String> GetCardsTerms()
    {
        LinkedHashSet<String> keys = new LinkedHashSet(terms.keySet());
        return keys;
    }

    int getSize(){return terms.size();}
/*
    void fill() {
        int numCards = getNumCards();
        for (int curInpCard = 1; curInpCard <= numCards; curInpCard++) {
            System.out.printf("The card #%d:\n", curInpCard);
            String cardTerm = askCardTerm();
            System.out.printf("The definition of the card #%d:\n",curInpCard);
            String cardDef = askCardDef();
            add(cardTerm, cardDef);
        }
    }
 */
/*
    void askQuestions()
    {
        Set<String> termSet = terms.keySet();
        for(String str : termSet)
        {
            System.out.printf("Print the definition of \"%s\":\n",str);
            String temp = input.nextLine();
            String cardTerm = getCardTermFromDefinition(temp);
            if(cardTerm.equals("???!!!???")) {
                System.out.printf("Wrong answer. The correct one is \"%s\".\n",terms.get(str));
            }else{
                if(str.equals(cardTerm))
                {
                    System.out.println("Correct answer.");
                }else {
                    System.out.printf("Wrong answer. The correct one is \"%s\", you've just " +
                            "written the definition of \"%s\".\n", terms.get(str), cardTerm);
                }
            }
        }
    }
 */
/*
    int getNumCards(){
        System.out.println("Input the number of cards:");
        String temp = input.nextLine();
        int num = Integer.parseInt(temp);
        return num;
    }
 */
/*
    String askCardTerm()
    {
        while(true)
        {
            String cardTerm = input.nextLine();
            if (isCardTermExists(cardTerm)) {
                System.out.printf("The card \"%s\" already exists. Try again:\n", cardTerm);
            }else{
                return cardTerm;
            }
        }
    }

 */
/*
    String askCardDef()
    {
        while(true)
        {
            String cardDef = input.nextLine();
            if (isCardDefExists(cardDef)) {
                System.out.printf("The definition \"%s\" already exists. Try again:\n", cardDef);
            } else {
                return cardDef;
            }
        }
    }

 */
}

class DeckMenu
{
    Scanner input;
    boolean isExit = false;
    DeckFlashCards deck;
    final String[] menuItems = {"add","remove","import","export","ask","exit"};
    DeckMenu(DeckFlashCards deck)
    {
        this.deck = deck;
        ///        input = inp;
        input = new Scanner(System.in);
    }
    void userAskingLoop()
    {
        while(!isExit) {
            printMenu();
            userInput();
        }
    }

    void printMenu(){
        System.out.print("Input the action (");
        System.out.print(String.join(",", menuItems));
        System.out.println("):");
    }
    void userInput(){
        String cmd = input.nextLine();
        int cmdInd = getCmdInd(cmd);
        if(cmdInd == 0) // add command
        {
            ExecuteAddCmd();
        }
        else if(cmdInd == 1)    //remove command
        {
            ExecuteRemoveCmd();
        }
        else if(cmdInd == 2)   // import command
        {
            ExecuteImportCmd();
        }
        else if(cmdInd == 3)   // export command
        {
            ExecuteExportCmd();
        }
        else if(cmdInd == 4) // ask command
        {
            ExecuteAskCmd();
        }
        else if(cmdInd == 5) // exit command
        {
            isExit = true;
            System.out.println("Bye bye!");
        }
        else // undfined command
        {
            System.out.println("Undefined command");
        }
    }
    private void ExecuteAddCmd(){
        System.out.println("The card:");
        String cardTerm = input.nextLine();
        if(deck.isCardTermExists(cardTerm))
        {
            System.out.printf("The card \"%s\" already exists.\n",cardTerm);
        }else {
            System.out.println("The definition of the card:");
            String cardDef = input.nextLine();
            if(deck.isCardDefExists(cardDef))
            {
                System.out.printf("The definition \"%s\" already exists\n",cardDef);
            }else{
                deck.add(cardTerm,cardDef);
                System.out.printf("The pair (\"%s\":\"%s\") has been added.\n",cardTerm,cardDef);
            }
        }
    }
    private void ExecuteRemoveCmd(){
        System.out.println("The card:");
        String cardTerm = input.nextLine();
        if(deck.isCardTermExists(cardTerm))
        {
            deck.removeCard(cardTerm);
            System.out.println("The card has been removed");
        }else{
            System.out.printf("Can't remove \"%s\": there is no such card.",cardTerm);
        }
    }
    private void ExecuteExportCmd(){
        System.out.println("File name:");
        String fileName = input.nextLine();
        File expFile = new File(fileName);
        try(PrintWriter wrExpFile = new PrintWriter(expFile)){
            Set<String> terms = deck.GetCardsTerms();
            for(String term : terms)
            {
                String definition = deck.getCardDefinitionFromTerm(term);
                wrExpFile.println(term);
                wrExpFile.println(definition);
            }
            System.out.println(terms.size() + " cards have been saved.");
        }catch(IOException ex){
            //System.out.println(ex.toString());
            System.out.println("File not found.");
        }
    }
    private void ExecuteImportCmd(){
        System.out.println("File name:");
        String fileName = input.nextLine();
        File impfile = new File(fileName);
        try(Scanner fileReader = new Scanner(impfile)){
            int counter = 0;
            while(fileReader.hasNext())
            {
                String term = fileReader.nextLine();
                String defenition = fileReader.nextLine();
                if(deck.isCardTermExists(term))
                {
                    deck.replaceDefinition(term,defenition);
                }else{
                    deck.add(term,defenition);
                }
                counter++;
            }
            System.out.println(counter + " cards have been loaded.");
        }catch(IOException ex){
            System.out.println("File not found.");
        }
    }
    private void ExecuteAskCmd(){
        System.out.println("How many times to ask?");
        int num = Integer.parseInt(input.nextLine());
        Set<String> setTerms =  deck.GetCardsTerms();
        String[] terms = setTerms.toArray(new String[setTerms.size()]);
        // use random to select questions!!!
        Random rnd = new Random();
        while(num > 0)
        {
            int indQuestion = rnd.nextInt(setTerms.size());
            String currentTerm = terms[indQuestion];
            System.out.printf("Print the definition of \"%s\":\n",currentTerm);
            System.out.flush();
            String temp = input.nextLine();

            String findTerm = deck.getCardTermFromDefinition(temp);
            if(findTerm.equals("???!!!???")) {
                System.out.printf("Wrong answer. The correct one is \"%s\".\n",deck.getCardDefinitionFromTerm(currentTerm));
            }else{
                if(terms[indQuestion].equals(findTerm))
                {
                    System.out.println("Correct answer.");
                }else {
                    System.out.printf("Wrong answer. The correct one is \"%s\", you've just " +
                            "written the definition of \"%s\".\n", deck.getCardDefinitionFromTerm(currentTerm), findTerm);
                }
            }
            num--;
        }
    }

    private int getCmdInd(String cmd){
        int i = 0;
        for(; i < menuItems.length; i++)
        {
            if(menuItems[i].equals(cmd))
                break;
        }
        return i == menuItems.length ? -1 : i;
    }
}
