//
// Created by Vincent on 2021/5/30.
//

#include "BubbleSort.h"
#include "CommonUtil.h"
#include <stdio.h>

void bubbleSortForIntTest() {

    int arrLength = 11;
    int arr[] = {1, 5, 9, 3, 4, 5, 8, 6, 6, 4, 6};
    ascSorted(arrLength, arr);

    bubbleSortForInt(arrLength, arr);
    ascSorted(arrLength, arr);

    for (int i = 0; i < arrLength; ++i) {
        printf("%d\t", arr[i]);
    }
    printf("\n");
}

int main() {
    bubbleSortForIntTest();
    return 0;
}

