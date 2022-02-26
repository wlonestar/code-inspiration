/**
 * 从整数序列中，找出总和最大的区段
 * @Author: com.wjl 
 * @Date: 2021-07-12 00:08:19 
 * @Last Modified by: com.wjl
 * @Last Modified time: 2021-07-12 00:10:25
 */

#include <iostream>
#include <ctime>
using namespace std;

/**
 * 三层循环
 * O(n^3)
*/
int gs_BF(int A[], int n)
{
    int gs = A[0];
    for (int i = 0; i < n; i++) {
        for (int j = i; j < n; j++) {
            int s = 0;
            for (int k = i; k <= j; k++) s += A[k];
            if (gs < s) gs = s;
        }
    }
    return gs;
}

/**
 * 递增策略
 * O(n^2)
*/
int gs_IC(int A[], int n)
{
    int gs = A[0];
    for (int i = 0; i < n; i++) {
        int s = 0;
        for (int j = i; j < n; j++) {
            s += A[j];
            if (gs < s) gs = s;
        }
    }
    return gs;
}

/**
 * 分治策略
 * O(nlog n)
*/
int gs_DC(int A[], int lo, int hi)
{
    if (hi - lo < 2) return A[lo];
    int mi = (lo + hi) / 2;
    int gsL = A[mi - 1], sL = 0, i = mi;
    while (lo < i--) {
        if (gsL < (sL += A[i])) gsL = sL;
    }
    int gsR = A[mi], sR = 0, j = mi - 1;
    while (++j < hi) {
        if (gsR < (sR += A[j])) gsR = sR;
    }
    return max(gsL + gsR, max(gs_DC(A, lo, mi), gs_DC(A, mi, hi)));
}

/**
 * 减治策略
 * O(n)
*/
int gs_LS(int A[], int n)
{
    int gs = A[0], s = 0, i = n;
    while (0 < i--) {
        s += A[i];
        if (gs < s) gs = s;
        if (s < 0)   s = 0;
    }
    return gs;
}

int main(int argc, char* argv[])
{
    int a[20] = {1, -2, 7, 2, 6, -9, 5, 6, -12, -8, 13, 0, -3, 1, -2, 8, 0, -5, 3};
    
    cout << "O(n^3):"; 
    clock_t begin1 = clock();
    for (int i = 0; i < 100000; i++) gs_BF(a, 20);
    clock_t end1 = clock();
    cout << (double)(end1 - begin1) / CLOCKS_PER_SEC << "\n";

    cout << "O(n^2):"; 
    clock_t begin2 = clock();
    for (int i = 0; i < 100000; i++) gs_IC(a, 20);
    clock_t end2 = clock();
    cout << (double)(end2 - begin2) / CLOCKS_PER_SEC << "\n";

    cout << "O(nlog n):"; 
    clock_t begin3 = clock();
    for (int i = 0; i < 100000; i++) gs_DC(a, 0, 19);
    clock_t end3 = clock();
    cout << (double)(end3 - begin3) / CLOCKS_PER_SEC << "\n";
    
    cout << "O(n):"; 
    clock_t begin4 = clock();
    for (int i = 0; i < 100000; i++) gs_DC(a, 0, 19);
    clock_t end4 = clock();
    cout << (double)(end4 - begin4) / CLOCKS_PER_SEC << "\n";

    return 0;
}
