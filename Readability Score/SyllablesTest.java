import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class SyllablesTest {
    
    static int numChar;
    static int numSyllables;
    
    public static void main(String[] args){
        Scanner usrInp =  new Scanner(System.in);
        System.out.println("Please enter observe string:");
        String temp = usrInp.nextLine();
        
        cntParam(temp);
        
        System.out.println("User input world: " + temp);
        System.out.println("Num characters:   " + numChar);
        System.out.println("Num syllables:    " + numSyllables);
    }
    
    public static void cntParam(String str) {
        Pattern cntVowells = Pattern.compile("[aeiouy]",Pattern.CASE_INSENSITIVE);
        Pattern cntDblVovells = Pattern.compile("[aeiouy]{2}",Pattern.CASE_INSENSITIVE);
       
        str = str.replaceAll("[.,?!]","").toLowerCase();
        
        Matcher m = cntVowells.matcher(str);
        numChar = str.length();
        //cnt Vowels
        numSyllables = 0;
        while(m.find()) {
            numSyllables++;
        }
        m = cntDblVovells.matcher(str);
        //exclude double vowels
        while(m.find()) {
            numSyllables--;
        }
        //exclude last e
        if(str.charAt(str.length() - 1) == 'e') {
            int ind = str.length() - 2;
            if(!m.find(ind))    //except last two syllables
                numSyllables--;
        }
        
        if(numSyllables == 0) {
            numSyllables = 1;
        }
        
    }
}

