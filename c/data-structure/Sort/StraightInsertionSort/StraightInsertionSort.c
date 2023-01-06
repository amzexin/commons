//
// Created by 李泽鑫 on 2021/5/25.
//

#include "StraightInsertionSort.h"

void straightInsertionSortInt(int arrLength, int arr[arrLength]) {
    for (int i = 1; i < arrLength; ++i) {
        int preInsertElement = arr[i];
        int preInsertIndex = i;
        for (int j = i - 1; j >= 0; --j) {
            if (arr[j] < preInsertElement) {
                break;
            }
            arr[j + 1] = arr[j];
            preInsertIndex = j;
        }
        arr[preInsertIndex] = preInsertElement;
    }
}