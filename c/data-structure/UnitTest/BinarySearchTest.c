//
// Created by Vincent on 2021/5/17.
//

#include "BinarySearch.h"
#include "CommonUtil.h"

int main() {
    int arrLength = 10;
    int arr[arrLength];
    for (int i = 0; i < arrLength; ++i) {
        arr[i] = i * i;
    }
    int searchElement = 25;

    int elementPos = binarySearchInt(arrLength, arr, searchElement);
    printf("%d所在位置为: %d", searchElement, elementPos);

    return 0;
}