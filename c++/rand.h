// 随机数生成
// Created by wjl on 2022/2/27.
//

#ifndef RAND_H
#define RAND_H

#pragma once

#include <ctime>
#include <cstdlib>

static int    dice(int range)      { return rand() % range; }
static int    dice(int lo, int hi) { return lo + rand() % (hi - lo); }
static float  dice(float range)    { return rand() % (1000 * (int) range) / (float) 1000.; }
static double dice(double range)   { return rand() % (1000 * (int) range) / (double) 1000.; }
static char   dice()               { return (char) (32 + rand() % 96); }

#endif //RAND_H
