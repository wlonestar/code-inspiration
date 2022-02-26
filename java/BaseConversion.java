import java.util.Stack;

/**
 * 进制转换
 *
 * @author: wjl
 * @date: 2022/1/18 12:20
 * @version: v1.0
 */
public class BaseConversion {

    static final char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    void convert(Stack<Character> S, long n, int base) {
        if (0 < n) {
            convert(S, n / base, base);
            S.push(digit[(int) (n % base)]);
        }
    }

    /**
     * 十进制数 {@code n} 到 {@code base} 进制的转换（递归版）
     *
     * @param n n
     * @param base base
     * @return char[]
     */
    char[] convert(long n, int base) {
        Stack<Character> result = new Stack<>();
        convert(result, n, base);
        char[] chars = new char[result.size()];
        int i = result.size() - 1;
        while (!result.isEmpty()) chars[i--] = result.pop();
        return chars;
    }

    /**
     * 十进制数 {@code n} 到 {@code base} 进制的转换（迭代版）
     *
     * @param n n
     * @param base base
     * @return char[]
     */
    char[] convertI(long n, int base) {
        Stack<Character> result = new Stack<>();
        while (n > 0) {
            result.push(digit[(int) (n % base)]);
            n /= base;
        }
        char[] chars = new char[result.size()];
        int i = 0;
        while (!result.isEmpty()) chars[i++] = result.pop();
        return chars;
    }

    public static void testBaseConvert(long n, int base) {
        System.out.println(new BaseConversion().convert(n, base));
        System.out.println(new BaseConversion().convertI(n, base));
    }

}
