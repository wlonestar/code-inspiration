/**
 * 归并排序
 * @Author: com.wjl 
 * @Date: 2021-07-12 00:13:04 
 * @Last Modified by: com.wjl
 * @Last Modified time: 2021-07-12 00:15:23
 */

#include <iostream>
#include <ctime>
using namespace std;

template <typename T>
void merge(T* a, int lo, int mi, int hi)
{
    int i = 0, j = 0, k = 0, lb = mi - lo, lc = hi - mi; 
    T* A = a + lo;
    T* B = new T[lb];
    T* C = a + mi;
    for (int i = 0; i < lb; i++) B[i] = A[i];
    while ((j < lb) && (k < lc)) A[i++] = (B[j] <= C[k]) ? B[j++] : C[k++];
    while (j < lb)               A[i++] = B[j++];
    delete [] B;
}

template <typename T>
void mergeSort(T* a, int lo, int hi)
{
    if (hi - lo < 2) return;
    int mi = (lo + hi) >> 1;
    mergeSort(a, lo, mi);
    mergeSort(a, mi, hi);
    merge(a, lo, mi, hi);
}

int main(int argc, char* argv[])
{
    int n = 1000;
    int a[1000];
    for (int i = 0; i < 10; i++) a[i] = rand();

    clock_t begin1 = clock();
    for (int i = 0; i < 1000000; i++) mergeSort(a, 0, 999);
    clock_t end1 = clock();
    cout << (double)(end1 - begin1) / CLOCKS_PER_SEC << "\n";

    return 0;
}
