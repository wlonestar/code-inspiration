/**
 * 二分查找
 * @Author: com.wjl 
 * @Date: 2021-07-12 00:11:08 
 * @Last Modified by: com.wjl
 * @Last Modified time: 2021-07-12 00:11:37
 */

#include <iostream>
#include <ctime>
using namespace std;

template <typename T>
int binSearch(T* S, T const& e, int lo, int hi)
{
    while (lo < hi) {
        int mi = (lo + hi) >> 1;
        if      (e < S[mi]) hi = mi;
        else if (e > S[mi]) lo = mi + 1;
        else                return mi;
    }
    return -1;
}

int main(int argc, char* argv[])
{
    int a[] = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

    clock_t begin1 = clock();
    for (int i = 0; i < 1000000; i++) binSearch(a, 3, 0, 9);
    clock_t end1 = clock();
    cout << (double)(end1 - begin1) / CLOCKS_PER_SEC << "\n";

    return 0;
}