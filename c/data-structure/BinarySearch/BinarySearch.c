//
// Created by Vincent on 2021/5/23.
//

#include "BinarySearch.h"
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

bool sortedArr(int arrLength, const int arr[arrLength]) {
    for (int i = 0; i < arrLength - 1; ++i) {
        if (arr[i] > arr[i + 1]) {
            return false;
        }
    }
    return true;
}

int binarySearchInt(int arrLength, int arr[arrLength], int searchElement) {
    if (!sortedArr(arrLength, arr)) {
        printf("数组无序，无法使用二分查找");
        exit(1);
    }

    int low = 0;
    int height = arrLength - 1;
    while (low < height) {
        int mid = (low + height) / 2;
        int compareResult = searchElement - arr[mid];
        if (compareResult == 0) {
            return mid;
        } else if (compareResult < 0) {
            height = mid - 1;
        } else if (compareResult > 0) {
            low = mid + 1;
        }
    }

    return -1;
}