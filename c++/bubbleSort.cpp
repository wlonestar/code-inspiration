/**
 * 冒泡排序
 * @Author: com.wjl 
 * @Date: 2021-07-12 00:12:10 
 * @Last Modified by: com.wjl
 * @Last Modified time: 2021-07-12 00:12:41
 */

#include <iostream>
#include <ctime>
using namespace std;

template <typename T>
void bubbleSortA(T* a, int lo, int hi)
{
    for (bool sorted = false; sorted = !sorted; ) {
        for (int i = lo; i < hi - 1; i++) {
            if (a[i] > a[i + 1]) {
                swap(a[i], a[i + 1]);
                sorted = false;
            }
        }
    }
}

int main(int argc, char* argv[])
{
    int a[] = {2, 5, 7, 8, 6, 4, 9, 1, 3, 10};

    clock_t begin1 = clock();
    for (int i = 0; i < 1000000; i++) bubbleSortA(a, 0, 9);
    clock_t end1 = clock();
    cout << (double)(end1 - begin1) / CLOCKS_PER_SEC << "\n";

    return 0;
}
