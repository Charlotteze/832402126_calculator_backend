package com.example.calculator.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Safe mathematical expression evaluator based on the
 * Shunting-yard algorithm (two stacks: operands and operators).
 *
 * <p>Supports: + - * /, parentheses, unary plus/minus, decimal numbers,
 * operator precedence, division-by-zero detection and invalid-expression detection.
 *
 * <p>User input is NEVER executed as code; it is parsed and evaluated
 * step by step, which satisfies the assignment's no-eval requirement.
 */
public final class ExpressionEvaluator {

    private static final MathContext MC = MathContext.DECIMAL64;

    private ExpressionEvaluator() {
    }

    /**
     * Evaluates an arithmetic expression and returns the result as a plain string,
     * e.g. "20", "0.5", "-6".
     *
     * @throws InvalidExpressionException if the expression is invalid
     */
    public static String evaluate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new InvalidExpressionException("Expression is empty");
        }

        Deque<BigDecimal> values = new ArrayDeque<>();
        Deque<Character> ops = new ArrayDeque<>();
        String s = expression.replace(" ", "");
        int i = 0;
        boolean expectOperand = true;

        while (i < s.length()) {
            char c = s.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                int j = i;
                boolean hasDot = false;
                while (j < s.length() && (Character.isDigit(s.charAt(j)) || s.charAt(j) == '.')) {
                    if (s.charAt(j) == '.') {
                        if (hasDot) {
                            throw new InvalidExpressionException("Invalid number format");
                        }
                        hasDot = true;
                    }
                    j++;
                }
                String number = s.substring(i, j);
                if (number.endsWith(".")) {
                    number = number + "0";
                }
                try {
                    values.push(new BigDecimal(number));
                } catch (NumberFormatException e) {
                    throw new InvalidExpressionException("Invalid number: " + number);
                }
                i = j;
                expectOperand = false;
            } else if (c == '(') {
                ops.push(c);
                i++;
                expectOperand = true;
            } else if (c == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    apply(ops.pop(), values);
                }
                if (ops.isEmpty() || ops.pop() != '(') {
                    throw new InvalidExpressionException("Mismatched parentheses");
                }
                i++;
                expectOperand = false;
            } else if (isOperator(c)) {
                if (expectOperand && c == '-') {
                    // Unary minus: highest precedence, push marker '~'
                    while (!ops.isEmpty() && ops.peek() != '(' && precedence(ops.peek()) >= precedence('~')) {
                        apply(ops.pop(), values);
                    }
                    ops.push('~');
                    i++;
                } else if (expectOperand) {
                    throw new InvalidExpressionException("Unexpected operator '" + c + "'");
                } else {
                    while (!ops.isEmpty() && ops.peek() != '(' && precedence(ops.peek()) >= precedence(c)) {
                        apply(ops.pop(), values);
                    }
                    ops.push(c);
                    i++;
                    expectOperand = true;
                }
            } else {
                throw new InvalidExpressionException("Invalid character '" + c + "'");
            }
        }

        while (!ops.isEmpty()) {
            if (ops.peek() == '(') {
                throw new InvalidExpressionException("Mismatched parentheses");
            }
            apply(ops.pop(), values);
        }

        if (values.size() != 1) {
            throw new InvalidExpressionException("Invalid expression");
        }
        return format(values.pop());
    }

    private static void apply(char op, Deque<BigDecimal> values) {
        if (op == '~') {
            if (values.isEmpty()) {
                throw new InvalidExpressionException("Invalid expression");
            }
            values.push(values.pop().negate());
            return;
        }
        if (values.size() < 2) {
            throw new InvalidExpressionException("Invalid expression");
        }
        BigDecimal right = values.pop();
        BigDecimal left = values.pop();
        switch (op) {
            case '+':
                values.push(left.add(right));
                break;
            case '-':
                values.push(left.subtract(right));
                break;
            case '*':
                values.push(left.multiply(right));
                break;
            case '/':
                if (right.compareTo(BigDecimal.ZERO) == 0) {
                    throw new InvalidExpressionException("Division by zero");
                }
                values.push(left.divide(right, MC));
                break;
            default:
                throw new InvalidExpressionException("Unknown operator '" + op + "'");
        }
    }

    private static int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '~':
                return 3;
            default:
                return 0;
        }
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private static String format(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "0";
        }
        return value.stripTrailingZeros().toPlainString();
    }
}
