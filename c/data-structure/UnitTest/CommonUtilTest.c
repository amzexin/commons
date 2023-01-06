//
// Created by Vincent on 2021/5/17.
//

#include "CommonUtil.h"

void toStringTest() {
    Student *student = malloc(sizeof(Student));
    student->id = "001";
    student->name = "张三";

    char *toString = toStringStudent(student);
    printf("%s", toString);
}

void arrTest() {
    int arrLength = 10;
    int arr[arrLength];
    for (int i = 0; i < arrLength; ++i) {
        arr[i] = i * i;
    }

    int what = arr[8] - arr[0];
    printf("arr[8] - arr[0] = %d \n", what);
}

void arrPointerTest() {
    int arrLength = 10;
    int *arrp = malloc(sizeof(int) * arrLength);

    for (int i = 0; i < arrLength; ++i) {
        arrp[i] = i * i;
    }

    int what = arrp[8] - arrp[0];
    printf("arrp[8] = %d, arrp[0] = %d, arrp[8] - arrp[0] = %d \n", arrp[8], arrp[0], what);

    what = (arrp + 8) - arrp;
    printf("(arrp + 8) = %d, arrp = %d, (arrp + 8) - arrp = %d \n", (arrp + 8), arrp, what);

    what = *(arrp + 8) - *arrp;
    printf("*(arrp + 8) = %d, *arrp = %d, *(arrp + 8) - *arrp = %d \n", *(arrp + 8), *arrp, what);
}

void stringTest() {
    char *str = "0123456789";
    printf("str = %s\n", str);
    printf("str + 7 = %s\n", str + 7);
}

void fullOfWater(int cupCapacity, int outWaterSpeed) {
    printf("开始水满实验 ==> 杯容量:%dL, 出水速度:%d\n", cupCapacity, outWaterSpeed);
    int cupUsedCapacity = 0; // 杯中水量
    int timeOfSeconds = 0; // 时间流逝
    while (cupUsedCapacity < cupCapacity) {
        // 时间1min 1min的走
        timeOfSeconds++;
        // 计算这一分钟过后，杯中能有多少水
        int remainingWater = timeOfSeconds - outWaterSpeed;
        cupUsedCapacity += remainingWater;
        if (cupUsedCapacity < 0) {
            cupUsedCapacity = 0;
        }
        printf("%d分钟之后, 水杯里的水达到%dL\n", timeOfSeconds, cupUsedCapacity);
    }
    printf("SO ==> %d分钟之后, 水杯将保持满水状态\n", timeOfSeconds);
}

int main() {
//    arrTest();
//    arrPointerTest();
//    stringTest();
    /**
     * 开始水满实验 ==> 杯容量:10L, 出水速度:3
     * 1分钟之后, 水杯里的水达到0L
     * 2分钟之后, 水杯里的水达到0L
     * 3分钟之后, 水杯里的水达到0L
     * 4分钟之后, 水杯里的水达到1L
     * 5分钟之后, 水杯里的水达到3L
     * 6分钟之后, 水杯里的水达到6L
     * 7分钟之后, 水杯里的水达到10L
     * SO ==> 7分钟之后, 水杯将保持满水状态
     */
    fullOfWater(10, 3);
    return 0;
}