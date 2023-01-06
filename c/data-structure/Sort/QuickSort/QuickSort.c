//
// Created by Vincent on 2021/5/30.
//

#include "QuickSort.h"

void swapValue(int arr[], int i, int j) {
    int temp = arr[i];
    arr[i] = arr[j];
    arr[j] = temp;
}

// [left, right]
void quickSortForIntByRecursion(int arr[], int left, int right) {
    // 递归结束
    if (left >= right) {
        return;
    }

    // partition
    int middleValue = arr[left];
    int i = left + 1;
    int j = right;
    while (i < j) {
        if (arr[i] <= middleValue) {
            i++;
            continue;
        }
        if (arr[j] > middleValue) {
            j--;
            continue;
        }
        swapValue(arr, i, j);
        i++;
        j--;
    }
    int middleIndex = arr[i] < middleValue ? i : i - 1;
    swapValue(arr, left, middleIndex);

    // 左半部分和右半部分继续递归
    quickSortForIntByRecursion(arr, left, middleIndex - 1);
    quickSortForIntByRecursion(arr, middleIndex + 1, right);
}

// 根据书上的逻辑自己写的一遍
void quickSortForIntByRecursion2(int arr[], int left, int right) {
    // 递归终止
    if (left >= right) {
        return;
    }

    // partition
    int middleIndex = left;
    int i = left + 1;
    int j = right;
    while (1) {
        // j从后往前遍历，找到一个小于基准值的数字
        while (j > middleIndex && arr[j] >= arr[middleIndex]) { j--; }
        if (j <= middleIndex) {
            break;
        }
        // 交换基准值和小于基准值的数字
        swapValue(arr, middleIndex, j);
        middleIndex = j;

        // i从前往后遍历，找到一个大于基准值的数字
        while (i < middleIndex && arr[i] <= arr[middleIndex]) { i++; }
        if (i >= middleIndex) {
            break;
        }
        // 交换基准值和大于基准值的数字
        swapValue(arr, middleIndex, i);
        middleIndex = i;
    }

    // 递归求两边
    quickSortForIntByRecursion2(arr, left, middleIndex - 1);
    quickSortForIntByRecursion2(arr, middleIndex + 1, right);

}

// 书中的代码
void quickSortForIntByRecursion3(int arr[], int left, int right) {
    // 递归终止
    if (left >= right) {
        return;
    }

    // partition
    int x = arr[left];
    int i = left;
    int j = right;
    while (i < j) {
        // j从后往前遍历，找到一个小于基准值的数字
        while (i < j && arr[j] >= x) { j--; }
        arr[i] = arr[j];

        // i从前往后遍历，找到一个大于基准值的数字
        while (i < j && arr[i] <= x) { i++; }
        arr[j] = arr[i];
    }
    int middleIndex = i;
    arr[middleIndex] = x;

    // 递归求两边
    quickSortForIntByRecursion2(arr, left, middleIndex - 1);
    quickSortForIntByRecursion2(arr, middleIndex + 1, right);

}

void quickSortForInt(int arrLength, int arr[arrLength]) {
    quickSortForIntByRecursion3(arr, 0, arrLength - 1);
}


