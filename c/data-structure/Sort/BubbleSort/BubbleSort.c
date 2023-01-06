//
// Created by Vincent on 2021/5/30.
//

#include "BubbleSort.h"

void bubbleSortForInt(int arrLength, int arr[arrLength]) {
    for (int i = 1; i < arrLength; ++i) {
        int swapCount = 0;
        for (int j = 0; j < arrLength - i; ++j) {
            if (arr[j] > arr[j + 1]) {
                int temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
                swapCount++;
            }
        }
        if (swapCount == 0) {
            break;
        }
    }
}