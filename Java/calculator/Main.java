package calculator;

import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigInteger;

enum CalcState{
    NO_ERR,
    UNKNOWN_VAR_ERR,
    UNBALANCED_PARENTHESES_ERR,
    INVALID_EXPRESSION_ERR
}
public class Main {
    static LinkedHashMap<String, BigInteger> variables = new LinkedHashMap<>();
    static List<String> rpn = new ArrayList<>();
    static Deque<String> opStack = new ArrayDeque<>();
    static Deque<BigInteger> calcStack = new ArrayDeque<>();
    static CalcState state;
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
        return true;
    }

    public static void showHelp() {
        System.out.println("The Smart calculator program. Supports +,-,*,/ and even ^ operations");
    }

    public static void determineAction(String input) {
        if (input.contains("=")) {
            assign(input);
        } else {
            resetResult();
            input = prepareExpression(input);
            if(!isErrorOccur()){
                convertToRPN(input);
                if(!isErrorOccur()){
                    BigInteger result =  calcRPN();
                    if(!isErrorOccur()){
                        System.out.println(result);
                    }
                }
            }
            if(isErrorOccur())
                OutputError();
        }
    }
    private static boolean isErrorOccur()
    {
        return state != CalcState.NO_ERR;
    }

    private static void OutputError()
    {
        switch(state){
            case UNBALANCED_PARENTHESE_ERR:
            case INVALID_EXPRESSION_ERR:
                System.out.println("Invalid expression");
                break;
            case UNKNOWN_VAR_ERR:
                System.out.println("Unknown variable");
        }
    }

    private static BigInteger calcRPN() {
        for(String token : rpn) {
            if(isOperand(token)) {
                calcStack.offerFirst(new BigInteger(token));
            } else {
                if(calcStack.size() >= 2) {
                    BigInteger op2 = calcStack.pollFirst();
                    BigInteger op1 = calcStack.pollFirst();
                    switch (token) {
                        case "+":
                            op1 = op1.add(op2);
                            break;
                        case "-":
                            op1 = op1.subtract(op2);
                            break;
                        case "*":
                            op1 = op1.multiply(op2);
                            break;
                        case "/":
                            op1 = op1.divide(op2);
                            break;
                        case "^":
                            op1 = op1.pow(op2.intValue());
                            break;
                    }
                    calcStack.offerFirst(op1);
                } else if(calcStack.isEmpty()) {
                    state = CalcState.INVALID_EXPRESSION_ERR;
                    break;
                }
            }
        }
        return state == CalcState.NO_ERR ? calcStack.pollFirst() : BigInteger.ZERO;
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

        Pattern pattern = Pattern.compile( "((\\*\\s){2,})|((/\\s){2,})" );
        Matcher matcher = pattern.matcher(input);
        if(matcher.find()) {
            state = CalcState.INVALID_EXPRESSION_ERR;
        }
        return input;
    }
    private static void convertToRPN(String input) {
        String[] tokens = input.split("\\s+");  // naive method

        for(int i = 0; i < tokens.length; i++) {
            if(isOperand(tokens[i])) {
                String value = tokens[i];
                if(tokens[i].matches("[a-zA-Z]+"))
                {
                    if(variables.containsKey(value))
                        value = String.valueOf(variables.get(value));
                    else{
                        state = CalcState.UNKNOWN_VAR_ERR;
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


                    if(topStackOp.equals("(") || curOp.equals("(")) {        // 2  //5
                        opStack.offerFirst(curOp);
                    } else if(curOp.equals(")")) {      //6
                        if(!rightParenthisHandler())
                            break;
                    }
                    else if(curOpPrior > topStackOpPrior) {       // 3
                        opStack.offerFirst(curOp);
                    }  else if(curOpPrior <= topStackOpPrior) {      // 4
                        priorityCurOpLessTopStackHandler(topStackOpPrior,curOpPrior,topStackOp,curOp);
                    }
                }
            }
        }
        // flush stack
        if(state == CalcState.NO_ERR) {
            while (!opStack.isEmpty()) {              // 7
                String token = opStack.pollFirst();
                if(token.equals("(") || token.equals(")")) {
                    state = CalcState.UNBALANCED_PARENTHESES_ERR;
                    break;
                } else {
                    rpn.add(token);
                }
            }
        }
     ///   return rpn;
    }
    private static boolean rightParenthisHandler() {
        boolean findLeftParenthesis = false;
        while(!opStack.isEmpty()){
            String topStack = opStack.pollFirst();
            if(topStack.equals("(")){
                findLeftParenthesis = true;
                break;
            } else {
                rpn.add(topStack);
            }
        }
        if(!findLeftParenthesis)
            state = CalcState.UNBALANCED_PARENTHESE_ERR;
        return findLeftParenthesis;
    }

    private static void priorityCurOpLessTopStackHandler(int topStackPrior,int curOpPrior,String topStackOp,String curOp) {
        boolean putOpOnstack = false;
        while(curOpPrior <= topStackPrior){
            if(opStack.isEmpty() || topStackOp.equals("(")){
                opStack.offerFirst(curOp);
                putOpOnstack = true;
                break;
            } else {

                if( !topStackOp.equals("(") ) {
                    rpn.add(topStackOp);
                }
                opStack.pollFirst();
                if(opStack.isEmpty())
                    break;
                topStackOp = opStack.peekFirst();
                topStackPrior = getOpPriority(topStackOp);
            }
        }
        if(!putOpOnstack){
            opStack.offerFirst(curOp);
        }
    }
    private static void resetResult() {
        state = CalcState.NO_ERR;
        opStack.clear();
        calcStack.clear();
        rpn.clear();
    }

    public static boolean isOperand(String token) {
        return token.matches("([\\+\\-]?\\d|[a-zA-Z])+");
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

    public static void assign(String input) {
        String[] variableAndValue = input
                .replaceAll("\\+|--", "")
                .replaceAll("\\s", "")
                .split("=");
        if (variableAndValue[1].matches("-?\\d+")) {
            variables.put(variableAndValue[0],new BigInteger(variableAndValue[1]));
        } else {
            if (variables.containsKey(variableAndValue[1])) {
                variables.put(variableAndValue[0], variables.get(variableAndValue[1]));
            } else {
                System.out.println("Unknown variable");
            }
        }
    }
}