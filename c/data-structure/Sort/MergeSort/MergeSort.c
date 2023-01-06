//
// Created by 李泽鑫 on 2021/5/31.
//

#include <stdlib.h>
#include "MergeSort.h"

void mergeSortForInt_(int arr[], int left, int right) {
    // 递归终止条件
    if (left >= right) {
        return;
    }

    // 分别归并排序
    int middleIndex = (left + right) / 2;
    mergeSortForInt_(arr, left, middleIndex);
    mergeSortForInt_(arr, middleIndex + 1, right);

    // 合并
    int arrLength = right - left + 1;
    int *arrCpy = malloc(sizeof(int) * arrLength);
    int i = left;
    int j = middleIndex + 1;
    for (int k = 0; k < arrLength; ++k) {
        if (i > middleIndex) {
            arrCpy[k] = arr[j++];
        } else if (j > right) {
            arrCpy[k] = arr[i++];
        } else if (arr[i] <= arr[j]) {
            arrCpy[k] = arr[i++];
        } else {
            arrCpy[k] = arr[j++];
        }
    }
    for (int k = 0; k < arrLength; ++k) {
        arr[left + k] = arrCpy[k];
    }
    free(arrCpy);
}

void mergeSortForInt(int arrLength, int arr[arrLength]) {
    mergeSortForInt_(arr, 0, arrLength - 1);
}