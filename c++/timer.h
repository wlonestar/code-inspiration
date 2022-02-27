// 运行时间测试
// Created by wjl on 2022/2/27.
//

#ifndef TIMER_H
#define TIMER_H

#pragma once

#include <iostream>
using namespace std;
#include <chrono>

typedef chrono::time_point<chrono::system_clock, chrono::duration<double, milli>> timer_t;

class benchmark {
private:
    timer_t start; timer_t end{}; int t;
public:
    explicit benchmark(int t) {
        this->t = t; this->start = chrono::high_resolution_clock::now();
        printf(" start count %4d times\n", t);
    }
    double get() {
        this->end = chrono::high_resolution_clock::now();
        return chrono::duration<double, std::milli>(end - start).count() / (double) t;
    }
    void print() {
        this->end = chrono::high_resolution_clock::now();
        double count = chrono::duration<double, std::milli>(end - start).count() / (double) t;
        printf("   end count %4d times ===> cost %.4f ms", t, count);
    }
};

#endif //TIMER_H
