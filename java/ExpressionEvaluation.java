import java.util.*;

/**
 * @author: wjl
 * @date: 2022/1/18 12:27
 * @version: v1.0
 */
public class ExpressionEvaluation {

    // 运算符优先级表
    static final char[][] pri = {
            /*         +    -    *    /    %    ^    !    (    )    \0  */
            /* + */ { '>', '>', '<', '<', '<', '<', '<', '<', '>', '>' },
            /* - */ { '>', '>', '<', '<', '<', '<', '<', '<', '>', '>' },
            /* * */ { '>', '>', '>', '>', '<', '<', '<', '<', '>', '>' },
            /* / */ { '>', '>', '>', '>', '<', '<', '<', '<', '>', '>' },
            /* % */ { '>', '>', '>', '>', '<', '<', '<', '<', '>', '>' },
            /* ^ */ { '>', '>', '>', '>', '<', '>', '<', '<', '>', '>' },
            /* ! */ { '>', '>', '>', '>', '<', '>', '>', ' ', '>', '>' },
            /* ( */ { '<', '<', '<', '<', '<', '<', '<', '<', '=', ' ' },
            /* ) */ { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
            /*\0 */ { '<', '<', '<', '<', '<', '<', '<', '<', ' ', '=' }
    };

    /**
     * 运算符顺序
     *
     * @param c c
     * @return index
     */
    int operOfNum(char c) {
        return switch (c) {
            case '+' -> 0;
            case '-' -> 1;
            case '*' -> 2;
            case '/' -> 3;
            case '%' -> 4;
            case '^' -> 5;
            case '!' -> 6;
            case '(' -> 7;
            case ')' -> 8;
            case '\0' -> 9;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

    /**
     * 返回优先级
     *
     * @param op1 运算符1
     * @param op2 运算符2
     * @return {@code > or <}
     */
    private char orderBet(char op1, char op2) {
        return pri[operOfNum(op1)][operOfNum(op2)];
    }

    /**
     * 阶乘
     *
     * @param a a
     * @return {@code a!}
     */
    long fac(int a) {
        long result = 1;
        while (a > 1) result *= a--;
        return result;
    }

    /**
     * 二元运算符计算
     *
     * @param num1 操作数1
     * @param op 运算符
     * @param num2 操作数1
     * @return 结果
     */
    double calcu(double num1, char op, double num2) {
        return switch (op) {
            case '+' -> num1 + num2;
            case '-' -> num1 - num2;
            case '*' -> num1 * num2;
            case '/' -> num1 / num2;
            case '%' -> num1 % num2;
            case '^' -> Math.pow(num1, num2);
            default -> throw new IllegalStateException("Unexpected value: " + op);
        };
    }

    /**
     * 一元运算符计算
     *
     * @param op 运算符
     * @param d 操作数
     * @return 结果
     */
    double calcu(char op, double d) {
        if (op == '!') {
            return (double) fac((int) d);
        } else {
            throw new IllegalStateException("Unexpected value: " + op);
        }
    }

    /**
     * 表达式求值
     *
     * @param strings 表达式
     * @return 结果
     */
    double calculate(String[] strings, List<String> rpn) {
        Queue<String> string = new LinkedList<>(List.of(strings));
        for (int i = 0; i < 6; i++) string.add("\0");
        Stack<Double> opnd = new Stack<>();
        Stack<Character> optr = new Stack<>();
        optr.push('\0');
        while (!optr.empty()) {
            String s = string.peek();
            char c = s.charAt(0);
            if (Character.isDigit(c)) {
                opnd.push(Double.valueOf(s)); string.remove();
                rpn.add(String.valueOf(opnd.peek()));
            } else {
                switch (orderBet(optr.peek(), c)) {
                    case '<' -> { optr.push(c); string.remove(); }
                    case '=' -> { optr.pop(); string.remove(); }
                    case '>' -> {
                        char op = optr.pop();
                        rpn.add(String.valueOf(op));
                        if ('!' == op) opnd.push(calcu(op, opnd.pop()));
                        else {
                            double d2 = opnd.pop();
                            double d1 = opnd.pop();
                            opnd.push(calcu(d1, op, d2));
                        }
                    }
                    default -> System.exit(-1);
                }
            }
        }
        return opnd.pop();
    }

    double evaluate(String[] strings) {
        Queue<String> string = new LinkedList<>(List.of(strings));
        Stack<Double> stack = new Stack<>();
        while (!string.isEmpty()) {
            String s = string.remove();
            char c = s.charAt(0);
            if (Character.isDigit(c)) {
                stack.push(Double.valueOf(s));
            } else {
                if (c == '!') {
                    double d = stack.pop();
                    stack.push((double)fac((int)d));
                } else {
                    double d2 = stack.pop();
                    double d1 = stack.pop();
                    stack.push(calcu(d1, c, d2));
                }
            }
        }
        return stack.pop();
    }

    public static void testExpressionEvaluation(String[] strings) {
        List<String> rpn = new ArrayList<>();
        System.out.println(new ExpressionEvaluation().calculate(strings, rpn));
        System.out.println(new ExpressionEvaluation().evaluate(rpn.toArray(new String[0])));
    }

}
