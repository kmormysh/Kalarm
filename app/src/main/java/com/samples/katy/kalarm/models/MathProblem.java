package com.samples.katy.kalarm.models;

import java.util.Random;
import java.util.Stack;

public class MathProblem {
    public int getAnswer() {
        return answer;
    }

    public String getMathProblem() {
        return mathProblem;
    }

    private enum Operators {
        ADD, SUBTRACT, DIVIDE, MULTIPLY;

        @Override
        public String toString() {
            String operator = "";
            switch (ordinal()) {
                case 0:
                    operator = "+";
                    break;
                case 1:
                    operator = "-";
                    break;
                case 2:
                    operator = "/";
                    break;
                case 3:
                    operator = "*";
                    break;
            }
            return operator;
        }
    }

    private int answer;
    private String mathProblem;
    private Stack<String> stack = new Stack<>();

    public MathProblem(int difficulty) {
        mathProblem = "";
        answer = 0;
        createStack(difficulty);
        solveStack();
    }

    private void createStack(int difficulty) {
        Random random = new Random();

        if (difficulty == 1) {
            generateNumbersForAdditionOrSubtraction(Operators.values()[random.nextInt(2)]);
        } else if (difficulty == 2) {
            String operator = Operators.values()[random.nextInt(2)].toString();
            stack.push(operator);
            int number = random.nextInt(100);

            //Add values to final problem
            mathProblem += Integer.toString(number) + operator;

            stack.push(Integer.toString(number));
            generateNumbersForDivisionOrMultiplication(Operators.values()[random.nextInt(2) + 2]);
        } else if (difficulty == 3) {
            String operator = Operators.values()[random.nextInt(2)].toString();
            stack.push(operator);

            generateNumbersForDivisionOrMultiplication(Operators.values()[random.nextInt(2) + 2]);

            //Add values to final problem
            mathProblem += operator;

            generateNumbersForDivisionOrMultiplication(Operators.values()[random.nextInt(2) + 2]);
        }
    }

    private void generateNumbersForAdditionOrSubtraction(Operators operator) {
        Random random = new Random();
        stack.push(operator.toString());
        int number = random.nextInt(100);
        stack.push(Integer.toString(number));

        //Add values to final problem
        mathProblem += number + operator.toString();

        number = random.nextInt(100);
        stack.push(Integer.toString(number));

        //Add values to final problem
        mathProblem += number;
    }

    private void generateNumbersForDivisionOrMultiplication(Operators operator) {
        Random random = new Random();
        int numerator, denominator, result;
        denominator = random.nextInt(10) + 1;
        result = random.nextInt(10) + 1;
        numerator = denominator * result;

        stack.push(operator.toString());
        if (operator == Operators.DIVIDE) {
            stack.push(Integer.toString(numerator));
            stack.push(Integer.toString(denominator));

            //Add values to final problem
            mathProblem += Integer.toString(numerator) + operator.toString()
                    + Integer.toString(denominator);

        } else if (operator == Operators.MULTIPLY) {
            stack.push(Integer.toString(result));
            stack.push(Integer.toString(denominator));

            //Add values to final problem
            mathProblem += Integer.toString(result) + operator.toString()
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
                int result = token.compareTo(Operators.ADD.toString()) == 0 ? integerTwo + integerOne :
                        token.compareTo(Operators.SUBTRACT.toString()) == 0 ? integerTwo - integerOne :
                                token.compareTo(Operators.MULTIPLY.toString()) == 0 ? integerTwo * integerOne :
                                        integerTwo / integerOne;

                newStack.push(String.valueOf(result));
            }
        }

        answer = Integer.valueOf(newStack.pop());
    }

    private boolean isOperator(String token) {
        if (Operators.ADD.toString().equals(token)
                || Operators.SUBTRACT.toString().equals(token)
                || Operators.MULTIPLY.toString().equals(token)
                || Operators.DIVIDE.toString().equals(token)) {
            return true;
        }
        return false;
    }
}
