//Description: Calculator class that utilizes stack ADT to function as
// Reverse Polish Notation Calculator.
//Author: Amar Sahbazovic
//Date: 10/3/2023

import java.util.*;

public class calculator {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        boolean loopContinue = true;

        System.out.println("Please leave a space between characters. For example, '1 2 + 4 3 - / 5 *'");
        System.out.println("To exit the calculator, enter 'exit'.");

        do {
            Stack<Integer> stack = new Stack<>(); // Create a new stack for each expression.
            System.out.print("Enter a reverse Polish notation arithmetic expression (or 'exit' to quit): ");
            String expression = input.nextLine();

            if (expression.equalsIgnoreCase("exit")) {
                loopContinue = false;
                continue; // Skip further processing and exit the loop.
            }

            int result = calculateExpression(expression, stack);

            if (result == -1) {
                System.out.println("Error in postfix expression, please try again");
            } else {
                System.out.println("The expression you entered " + expression + " = " + result);
            }

        } while (loopContinue);

        input.close();
    }

    // This method will parse through our expression and either calculate result
    // or return error accordingly.
    private static int calculateExpression(String expression, Stack<Integer> stack) {

        String[] elements = expression.split(" ");

        for (String element : elements) {
            if (isOperator(element)) {
                int operand2 = stack.pop();
                Integer operand1 = stack.pop();
                // If an operand is missing and we can't evaluate expression,
                // then we return postfix notation error.
                if (operand1 == null) {
                    return -1;
                }
                // Verify if we have any negative operands.
                if (containsNegative(operand1, operand2)) {
                    return -1;
                }
                int result = evaluateOperation(operand1, operand2, element);
                if (result == -1) {
                    return -1;
                }
                stack.push(result);
            } else {
                stack.push(Integer.valueOf(element));
            }
        }

        // Our verification check that at the end of our operations we have
	// only 1 item remaining on the stack.
        if (stack.size() == 1) {
            return stack.pop();
        } else {
            return -1;
        }
    }

    // This method will check whether or not the operands are negative.
    private static boolean containsNegative(int operand1, int operand2) {
        return operand1 < 0 || operand2 < 0;
    }

    // This method checks if an element in the entered expression is an operator.
    private static boolean isOperator(String element) {
        return element.equals("+") || element.equals("-") || element.equals("*") || element.equals("/");
    }

    // This method will perform various artihmetic operations in a switch case
    // depending on the operator passed through.
    private static int evaluateOperation(int operand1, int operand2, String operator) {

        switch (operator) {

            case "+":
                return operand1 + operand2;

            case "-":
                return operand1 - operand2;

            case "*":
                return operand1 * operand2;

            case "/":
                if (operand2 == 0) {
                    return -1;
                }
                return operand1 / operand2;

            default:
                return -1;
        }
    }
}
