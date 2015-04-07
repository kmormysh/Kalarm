package com.samples.katy.kalarm.models;

import java.util.Random;
import java.util.Stack;

public class MathProblem {

    private int answer;
    private String mathProblem;
    private Stack<String> stack = new Stack<>();

    private final String OP_ADD = "+";
    private final String OP_SUBTRACT = "-";
    private final String OP_DIVIDE = "/";
    private final String OP_MULTIPLY = "*";

    private final String[] ADD_AND_SUBTRACT = {OP_ADD, OP_SUBTRACT};
    private final String[] DIVIDE_AND_MULTIPLY = {OP_DIVIDE, OP_MULTIPLY};

    public MathProblem(int difficulty) {
        mathProblem = "";
        answer = 0;
        createStack(difficulty);
        solveStack();
    }

    public String getMathProblem() {
        return mathProblem;
    }

    private void createStack(int difficulty) {
        Random random = new Random();

        if (difficulty == 1) {
            generateNumbersForAdditionOrSubtraction(ADD_AND_SUBTRACT[random.nextInt(2)]);
        } else if (difficulty == 2) {
            String operator = ADD_AND_SUBTRACT[random.nextInt(2)];
            stack.push(operator);
            int number = random.nextInt(100);

            //Add values to final problem
            mathProblem += Integer.toString(number) + operator;

            stack.push(Integer.toString(number));
            generateNumbersForDivisionOrMultiplication(DIVIDE_AND_MULTIPLY[random.nextInt(2)]);
        } else if (difficulty == 3) {
            String operator = ADD_AND_SUBTRACT[random.nextInt(2)];
            stack.push(operator);

            generateNumbersForDivisionOrMultiplication(DIVIDE_AND_MULTIPLY[random.nextInt(2)]);

            //Add values to final problem
            mathProblem += operator;

            generateNumbersForDivisionOrMultiplication(DIVIDE_AND_MULTIPLY[random.nextInt(2)]);
        }
        else {
            throw new UnsupportedOperationException();
        }
    }

    private void generateNumbersForAdditionOrSubtraction(String operator) {
        Random random = new Random();
        stack.push(operator);
        int number = random.nextInt(100);
        stack.push(Integer.toString(number));

        //Add values to final problem
        mathProblem += number + operator;

        number = random.nextInt(100);
        stack.push(Integer.toString(number));

        //Add values to final problem
        mathProblem += number;
    }

    private void generateNumbersForDivisionOrMultiplication(String operator) {
        Random random = new Random();
        int numerator, denominator, result;
        denominator = random.nextInt(10) + 1;
        result = random.nextInt(10) + 1;
        numerator = denominator * result;

        stack.push(operator);
        if (operator.equals(OP_DIVIDE)) {
            stack.push(Integer.toString(numerator));
            stack.push(Integer.toString(denominator));

            //Add values to final problem
            mathProblem += Integer.toString(numerator) + operator
                    + Integer.toString(denominator);

        } else if (operator.equals(OP_MULTIPLY)) {
            stack.push(Integer.toString(result));
            stack.push(Integer.toString(denominator));

            //Add values to final problem
            mathProblem += Integer.toString(result) + operator
                    + Integer.toString(denominator);
        }
    }

    private void solveStack() {
        int integerOne;
        int integerTwo;

        Stack<String> newStack = new Stack<>();

        while (stack.size() > 0){
            String token = stack.pop();
            if(!isOperator(token)){
                newStack.push(token);
            }else{
                integerOne = Integer.valueOf(newStack.pop());
                integerTwo = Integer.valueOf(newStack.pop());
                int result = token.compareTo(OP_ADD) == 0 ? integerTwo + integerOne :
                        token.compareTo(OP_SUBTRACT) == 0 ? integerTwo - integerOne :
                                token.compareTo(OP_MULTIPLY) == 0 ? integerTwo * integerOne :
                                        integerTwo / integerOne;

                newStack.push(String.valueOf(result));
            }
        }
        answer = Integer.valueOf(newStack.pop());
    }

    private boolean isOperator(String token) {
        return OP_ADD.equals(token) || OP_SUBTRACT.equals(token) ||
                OP_DIVIDE.equals(token) || OP_MULTIPLY.equals(token);
    }

    public boolean isCorrectAnswer(int userAnswer) {
        return answer == userAnswer;
    }
}
