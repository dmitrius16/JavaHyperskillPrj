package calculator;

import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;

public class Main {
    private static LinkedHashMap<String, Integer> variables = new LinkedHashMap<>();
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();

            if (!validate(input)) {
                continue;
            }

            switch (input) {
                case "/exit" :
                    System.out.println("Bye!");
                    return;
                case "/help" :
                    showHelp();
                    break;
                case "" :
                    break;
                default :
                    determineAction(input);
                    break;
            }
        }
    }
    public static boolean validate (String input) {
        //validate command
        if (input.matches("/.*")) {
            if (!input.matches("(/(help|exit))|^$")) {
                System.out.println("Unknown command");
                return false;
            }
        }
        // validate assignment
        if (input.matches(".*=.*")) {
            if (!input.split("=")[0].matches("[a-zA-Z]+\\s*")) {
                System.out.println("Invalid identifier");
                return false;
            }
            if (!input.split("=")[1].matches("\\s*(\\d+|[a-zA-Z]+)")
                    || input.matches(".*=.*(=.*)+")) {
                System.out.println("Invalid assignment");
                return false;
            }
        }
        // validate expression
        if (!input.matches("/.*")) {
            //if (!input.matches("(((\\d|[a-zA-Z])+ )*[+-]\\s*(\\d|[a-zA-Z])+(\\s+[+-]\\s+(\\d|[a-zA-Z])+)*)")
            //        && !input.matches("[a-zA-z]+\\s*(=\\s*([0-9 ]+|[a-zA-z]))*")){
            //        && !input.matches("^$")) {
            if (!input.matches("(((\\d|[a-zA-Z])+ )*[+-]+\\s*(\\d|[a-zA-Z])+(\\s+[+-]+\\s+(\\d|[a-zA-Z])+)*)")
                    && !input.matches("[a-zA-z]+\\s*(=\\s*([0-9 ]+|[a-zA-z]))*")
                    && !input.matches("^$")) {
                System.out.println("Invalid expression");
                return false;
            }
        }
        return true;
    }

    public static void showHelp() {
        System.out.println("The program calculates the sum of numbers");
    }

    public static void determineAction(String input) {
        if (input.contains("=")) {
            assign(input);
        } else {
            sum(input);
        }
    }
    public static List<String> convertToRPN(String input){
        List<String> rpn = new ArrayList<>();
        Deque<String> opStack = new ArrayDeque<>();
        //
        //prepare input for rpn
        //
        String[] tokens = input.split("\\s+");  // naive method

        for(int i = 0; i < tokens.length; i++) {
            if(isOperand(tokens[i])) {
                rpn.add(tokens[i]);
            } else if(isOperator(tokens[i])) {
                if(opStack.isEmpty())
                {
                    opStack.offerFirst(tokens[i]);
                } else {
                    String topStackOp = opStack.peekFirst();
                    int curOpPrior = getOpPriority(tokens[i]);
                    int topStackOpPrior = getOpPriority(topStackOp);
                    if(topStackOp.equals("(")) {                         // 2
                        opStack.offerFirst(tokens[i]);
                    } else if(curOpPrior > topStackOpPrior) {       // 3
                        opStack.offerFirst(tokens[i]);
                    } else if(curOpPrior <= topStackOpPrior) {      // 4
                        opStack.pollFirst();
                        rpn.add(topStackOp);
                        while(true) {
                            if(opStack.isEmpty())  // ???
                                break;              // incorrect expression
                            topStackOp = opStack.peekFirst();
                            topStackOpPrior = getOpPriority(topStackOp);
                            if(topStackOp.equals("(") || curOpPrior > topStackOpPrior){
                                opStack.offerFirst(tokens[i]);
                                break;
                            }else if(curOpPrior <= topStackOpPrior){
                                rpn.add(topStackOp);
                                opStack.pollFirst();
                            }
                        }
                    } else if(tokens[i].equals("(")) {      //5
                        opStack.offerFirst(tokens[i]);
                    } else if(tokens[i].equals(")")) {      //6
                        while(true){
                            rpn.add(topStackOp);
                            if(opStack.isEmpty())
                                // something go wrong
                                break;
                            else
                            {
                                topStackOp = opStack.pollFirst();
                                if(topStackOp.equals("("))
                                    break;
                                else
                                    rpn.add(topStackOp);
                            }
                        }
                    }

                }
            }
        }
        // flush stack
        while(!opStack.isEmpty()){              // 7
            rpn.add(opStack.pollFirst());
        }


        return res;

    }
    public static boolean isOperand(String token) {
        return token.matches("(\\d|[a-zA-Z])+");
    }
    public static boolean isOperator(String token) {
        return token.matches("[*+-/]");
    }


    public static int getOpPriority(String op) {
        int priority = 0;
        switch(op){
            case "+":
            case "-":
                priority = 1;
                break;
            case "*":
            case "/":
                priority = 2;
                break;
            case "^":
                priority = 3;
                break;
            case "(":
            case ")":
                priority = 4;
                break;
        }
        return priority;
    }

    public static void sum(String input) {
        int result = 0;
        String[] numbers = input
                .replaceAll("\\+|--", "")
                .replaceAll("\\s+", " ")
                .replaceAll("-\\s", "-")
                .split(" ");
        for (String n : numbers) {
            int nValue;
            if (n.matches("-?\\d+")) {
                nValue = Integer.parseInt(n);
            } else {
                String variable = n.replaceAll("-", "");
                if (variables.containsKey(variable)) {
                    nValue = n.matches("-[a-zA-Z]+")
                            ? -(variables.get(variable)) : variables.get(variable);
                } else {
                    System.out.println("Unknown variable");
                    return;
                }
            }
            result += nValue;
        }
        System.out.println(result);
    }

    public static void assign(String input) {
        String[] variableAndValue = input
                .replaceAll("\\+|--", "")
                .replaceAll("\\s", "")
                .split("=");
        if (variableAndValue[1].matches("-?\\d+")) {
            variables.put(variableAndValue[0], Integer.parseInt(variableAndValue[1]));
        } else {
            if (variables.containsKey(variableAndValue[1])) {
                variables.put(variableAndValue[0], variables.get(variableAndValue[1]));
            } else {
                System.out.println("Unknown variable");
            }
        }
    }
}