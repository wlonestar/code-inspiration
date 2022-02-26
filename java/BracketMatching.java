import java.util.Stack;

/**
 * 括号匹配
 *
 * @author: wjl
 * @date: 2022/1/18 12:23
 * @version: v1.0
 */
public class BracketMatching {

    /**
     * 删除 {@code exp[lo, hi]} 不含括号的最长前缀、后缀
     *
     * @param exp exp
     * @param lo lo
     * @param hi hi
     */
    void trim(char[] exp, int lo, int hi) {
        while ((lo <= hi) && (exp[lo] != '(') && (exp[lo] != ')')) lo++;
        while ((lo <= hi) && (exp[hi] != '(') && (exp[hi] != ')')) hi++;
    }

    /**
     * 切分 {@code exp[lo, hi]} ，使 {@code exp} 匹配仅当子表达式匹配
     *
     * @param exp exp
     * @param lo lo
     * @param hi hi
     * @return 匹配
     */
    int divide(char[] exp, int lo, int hi) {
        int mi = lo, crc = 1;
        while ((0 < crc) && (++mi < hi)) {
            if (exp[mi] == ')') crc--;
            if (exp[mi] == '(') crc++;
        }
        return mi;
    }

    /**
     * 检查表达式 {@code exp[lo, hi]} 是否括号匹配（递归版）
     * @param exp exp
     * @param lo lo
     * @param hi hi
     * @return 匹配
     */
    boolean paren(char[] exp, int lo, int hi) {
        trim(exp, lo, hi);
        if (lo > hi) return true;
        if (exp[lo] != '(') return false;
        if (exp[hi] != ')') return false;
        int mi = divide(exp, lo, hi);
        if (mi > hi) return false;
        return paren(exp, lo + 1, mi - 1) && paren(exp, mi + 1, hi);
    }

    /**
     * 括号匹配算法（迭代版）
     *
     * @param exp 表达式
     * @param lo 左下标
     * @param hi 右下标
     * @return 是否匹配
     */
    boolean parenI(char[] exp, int lo, int hi) {
        Stack<Character> stack = new Stack<>();
        for (int i = lo; i <= hi; i++) {
            switch (exp[i]) {
                case '(': case '[': case '{':
                    stack.push(exp[i]); break;
                case ')':
                    if ((stack.empty()) || '(' != stack.pop()) return false; break;
                case ']':
                    if ((stack.empty()) || '[' != stack.pop()) return false; break;
                case '}':
                    if ((stack.empty()) || '{' != stack.pop()) return false; break;
                default: break;
            }
        }
        return stack.empty();
    }

    public static void testBracketMatching(char[] exp) {
        System.out.println(new BracketMatching().paren(exp, 0, exp.length - 1));
        System.out.println(new BracketMatching().parenI(exp, 0, exp.length - 1));
    }

}
