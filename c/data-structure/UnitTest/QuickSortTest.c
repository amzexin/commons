//
// Created by Vincent on 2021/5/30.
//

#include "QuickSort.h"
#include "CommonUtil.h"
#include <stdio.h>

void quickSortForIntTest() {

    int arrLength = 11;
    int arr[] = {1, 5, 9, 3, 4, 5, 8, 6, 6, 4, 6};
    ascSorted(arrLength, arr);

    quickSortForInt(arrLength, arr);
    ascSorted(arrLength, arr);

    for (int i = 0; i < arrLength; ++i) {
        printf("%d\t", arr[i]);
    }
    printf("\n");
}

int main() {
    quickSortForIntTest();
    return 0;
}

