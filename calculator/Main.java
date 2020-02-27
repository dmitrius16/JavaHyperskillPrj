package calculator;

import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;

public class Main {
    static LinkedHashMap<String, Integer> variables = new LinkedHashMap<>();
    static List<String> rpn = new ArrayList<>();
    static Deque<String> opStack = new ArrayDeque<>();
    static Deque<Integer> calcStack = new ArrayDeque<>();
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


            //if (!input.matches("(((\\d|[a-zA-Z])+ )*[+-]\\s*(\\d|[a-zA-Z])+(\\s+[+-]\\s+(\\d|[a-zA-Z])+)*)")
            //        && !input.matches("[a-zA-z]+\\s*(=\\s*([0-9 ]+|[a-zA-z]))*")){
            //        && !input.matches("^$")) {
          /*  if (!input.matches("(((\\d|[a-zA-Z])+ )*[+-]+\\s*(\\d|[a-zA-Z])+(\\s+[+-]+\\s+(\\d|[a-zA-Z])+)*)")
                    && !input.matches("[a-zA-z]+\\s*(=\\s*([0-9 ]+|[a-zA-z]))*")
                    && !input.matches("^$")) {
                System.out.println("Invalid expression");
                return false;
            }
        }*/
        return true;
    }

    public static void showHelp() {
        System.out.println("The program calculates the sum of numbers");
    }

    public static void determineAction(String input) {
        if (input.contains("=")) {
            assign(input);
        } else {
            //sum(input);


            resetResult();
            input = prepareExpression(input);
            List<String> res = convertToRPN(input);
            res.forEach(el->System.out.print(el + " "));
            System.out.println(" ");
            System.out.println(calcRPN());

        }
    }

    public static int calcRPN() {
        for(String token : rpn) {
            if(isOperand(token)) {
                calcStack.offerFirst(Integer.parseInt(token));
            } else {
                int op2 = calcStack.pollFirst();
                int op1 = calcStack.pollFirst();
                switch(token){
                    case "+":
                        op1 = op1 + op2;
                        break;
                    case "-":
                        op1 = op1 - op2;
                        break;
                    case "*":
                        op1 = op1 * op2;
                        break;
                    case "/":
                        op1 = op1 / op2;
                        break;
                    case "^":
                        int res = 1;
                        for(int i = 0; i < op2;i++){
                            res *= op1;
                        }
                        op1 = res;
                        break;
                }
                calcStack.offerFirst(op1);
            }
        }
        return calcStack.pollLast();
    }
    private static String prepareExpression(String input){
        input = input.replaceAll("-{2}","+")
        .replaceAll("\\+{2,}","+")
        .replaceAll("\\s+","")
        .replaceAll("((?!\\d)|(?<!\\d))"," ")
        .replaceAll("\\( -\\s+","( -")
        .replaceAll("\\( +\\s+","( +").trim()
        .replaceFirst("^\\-\\s","\\-")
        .replaceFirst("^\\+\\s","\\-");
        return input;
    }
    private static List<String> convertToRPN(String input) {
        String[] tokens = input.split("\\s+");  // naive method

        for(int i = 0; i < tokens.length; i++) {
            if(isOperand(tokens[i])) {
                String value = tokens[i];
                if(tokens[i].matches("[a-zA-Z]+"))
                {
                    if(variables.containsKey(value))
                        value = String.valueOf(variables.get(value));
                    else{
                        System.out.println("Unknown variable");
                        //ToDO set global error !!!
                        break;
                    }
                }
                rpn.add(value);
            } else if(isOperator(tokens[i])) {
                if(opStack.isEmpty())
                {
                    opStack.offerFirst(tokens[i]);                      // 1
                } else {
                    String topStackOp = opStack.peekFirst();
                    String curOp = tokens[i];

                    int curOpPrior = getOpPriority(curOp);
                    int topStackOpPrior = getOpPriority(topStackOp);

                    if(topStackOp.equals("(")) {        // 2
                        opStack.offerFirst(curOp);
                    } else if(curOp.equals("(")) {      //5
                        opStack.offerFirst(curOp);
                    } else if(curOp.equals(")")) {      //6
                        rightParenthisHandler();
                    }
                    else if(curOpPrior > topStackOpPrior) {       // 3
                        opStack.offerFirst(curOp);
                    }  else if(curOpPrior <= topStackOpPrior) {      // 4
                        priorityCurOpLessTopStackHandler(topStackOpPrior,curOpPrior,curOp);
                    }
                }
            }
        }
        // flush stack
        while(!opStack.isEmpty()){              // 7
            rpn.add(opStack.pollFirst());
        }
        return rpn;
    }
    private static void rightParenthisHandler() {
        while(!opStack.isEmpty()){
            String topStack = opStack.pollFirst();
            if(topStack.equals("(")){
                break;
            } else {
                rpn.add(topStack);
            }
        }
    }

    private static void priorityCurOpLessTopStackHandler(int topStackPrior,int curOpPrior,String curOp) {
        while(curOpPrior <= topStackPrior){
            String topStackOp = opStack.pollFirst();
            if(!topStackOp.equals("(")) {
                rpn.add(topStackOp);
            }
            if(opStack.isEmpty() || topStackOp.equals("(")) {
                opStack.offerFirst(curOp);
                break;
            } else {
                topStackOp = opStack.peekFirst();
                topStackPrior = getOpPriority(topStackOp);
            }
        }
    }
    private static void resetResult() {
        opStack.clear();
        calcStack.clear();
        rpn.clear();
    }

    public static boolean isOperand(String token) {
        return token.matches("(\\d|[a-zA-Z])+");
    }



    public static boolean isOperator(String token) {
        return token.matches("[*+-^/()]");
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
/*
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
*/
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