package calculator;

import javax.swing.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Map;
import java.util.HashMap;

public class Main {

    public static final String COMMAND = "/.*";
    public static final String EXPRESSION = "[-+]?(\\d|[a-zA-Z])+(\\s+([-+]+\\s+[-+]?(\\d|[a-zA-Z])+)?)*"; //(ONLY addition/subtraction will work)
    public static final String ASSIGNMENTS =  "(^[a-zA-Z]+\\s*)(=\\s*[a-zA-Z]+\\s*=?)*(=\\s*([a-zA-Z]|\\d)+$)+";

    static Map<String,Integer> variables = new HashMap<>();
    static boolean errorCalc = false;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        Pattern comm = Pattern.compile(COMMAND);
        Pattern expr = Pattern.compile(EXPRESSION);
        Matcher commMatcher;
        Matcher exprMatcher;

        while (true) {
            String line = in.nextLine();        // Input
            commMatcher = comm.matcher(line);   // Is command?
            exprMatcher = expr.matcher(line);   // Or expression?
            switch (line) {
                case "/exit":
                    System.out.println("Bye!");
                    return;
                case "/help":
                    System.out.println("Calculating addition or subtraction");
                    break;
                case "":    //TODO: Get rid of this lame shit
                    break;
                default:
                    errorCalc = false;
                    if (commMatcher.matches()) {
                        System.out.println("Unknown command");
                    }
                    else if (exprMatcher.matches()) {
                        int result = calculateExpr(line);
                        if(!errorCalc)
                            System.out.println(result);
                    }
                    else if(line.indexOf('=')!=-1) { // assignment
                        evaluateExpr(line);
                    }
                    else {
                        System.out.println("Invalid expression");
                    }
            }
        }
    }

    static int calculateExpr(String expr) {
        String[] temp = expr.split("\\s+");
        int result = HandleOperand(temp[0]);
        if(!errorCalc)
        {
            for (int i = 1; i < temp.length; i += 2) {
                int operand = HandleOperand(temp[i+1]);
                if(errorCalc)
                {
                    result = 0;
                    break;
                }
                result = minusOrPlus(temp[i]) ? result - operand : result + operand;
            }
        }
        return result;
    }

    static int HandleOperand(String op){
        int value = 0;
        if(op.matches("\\d+"))
            value = Integer.parseInt(op);
        else {
            if(variables.containsKey(op)){
                value = variables.get(op);
            } else {
                errorCalc = true;
                System.out.println("Unknown variable");
            }
        }
        return value;
    }

    static void evaluateExpr(String expr) {
        //String exprMod = expr.replaceAll("(?==)|(?<==)"," ");
        String exprMod = expr.replaceAll("="," ");
        String[] temp = exprMod.split("(\\s+)");
/*
        //for debug
        for(int i = 0;i < temp.length; i++){
            System.out.println(temp[i]);
        }
*/
        int value = 0;
        if(temp.length >= 2)
        {
            if(temp[temp.length-1].matches("\\d+"))
            {
                value = Integer.parseInt(temp[temp.length - 1]);
            } else if(temp[temp.length - 1].matches("[a-zA-Z]+")){
                if(variables.containsKey(temp[temp.length - 1]))
                    value = variables.get(temp[temp.length - 1]);
                else
                    System.out.println("Unknown variable");
            } else {
                System.out.println("Invalid assignment");
            }

            for(int i = temp.length - 2; i >= 0;i--)
            {
                if(temp[i].matches(".*\\d+.*")){
                    if(i == 0)
                        System.out.println("Invalid identifier");
                    else
                        System.out.println("Invalid assignment");

                } else {
                    variables.put(temp[i],value);
                }
            }
        }
    }
    static boolean minusOrPlus(String operation) {
        operation = operation.replaceAll("-{2}", "+");
        return operation.contains("-");
    }
}
