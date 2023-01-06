//
// Created by Vincent on 2021/5/31.
//

#include "StraightSelectSort.h"

void swapValue(int arr[], int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}

void straightSelectSortInt(int arrLength, int arr[arrLength]) {
    for (int i = 0; i < arrLength - 1; ++i) {
        int minValueIndex = i;
        for (int j = i + 1; j < arrLength; ++j) {
            if (arr[j] < arr[minValueIndex]) {
                minValueIndex = j;
            }
        }
        swapValue(arr, i, minValueIndex);
    }
}