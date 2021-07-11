/**
 * 十进制转换成n进制(n <= 16)
 * @Author: com.wjl 
 * @Date: 2021-07-12 00:17:01 
 * @Last Modified by: com.wjl
 * @Last Modified time: 2021-07-12 00:18:29
 */

/*
 * @Author: com.wjl 
 * @Date: 2021-07-11 22:32:45 
 * @Last Modified by: com.wjl
 * @Last Modified time: 2021-07-11 22:36:52
 */

#include <iostream>
#include <stack>
using namespace std;

void convert(stack<char>& s, __int64 n, int base)
{
    char digit[] = "0123456789ABCDEF";
    while (n > 0) { s.push(digit[n % base]); n /= base; }
}

int main(int argc, char* argv[])
{
    int n = 123456;
    int base = 2;
    stack<char> s;
    convert(s, n, base);
    while (!s.empty()) { cout << s.top(); s.pop(); }
    return 0;
}
