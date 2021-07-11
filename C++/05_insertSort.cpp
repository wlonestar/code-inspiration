/**
 * 插入排序
 * @Author: com.wjl 
 * @Date: 2021-07-12 00:15:45 
 * @Last Modified by: com.wjl
 * @Last Modified time: 2021-07-12 00:16:17
 */

#include <iostream>
#include <ctime>
using namespace std;

template <typename T>
void insertSort(T* a, int lo, int hi)
{
    for (int i = lo + 1; i <= hi; i++) {
        if (a[i] < a[i - 1]) {
            T temp = a[i];
            int j = i;
            while (j > lo && temp < a[j - 1]) {
                a[j] = a[j - 1]; j--;
            }
            a[j] = temp;
        }
    }
}

int main(int argc, char* argv[])
{
    int n = 10;
    int a[10];
    for (int i = 0; i < 10; i++) a[i] = rand();

    clock_t begin1 = clock();
    for (int i = 0; i < 1000000; i++) insertSort(a, 0, 9);
    clock_t end1 = clock();
    cout << (double)(end1 - begin1) / CLOCKS_PER_SEC << "\n";

    return 0;
}
