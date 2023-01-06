//
// Created by 李泽鑫 on 2021/5/25.
//

#include "StraightInsertionSort.h"
#include <stdio.h>

void straightInsertionSortTest() {

    int arr[] = {1, 5, 9, 3, 4, 5, 8, 6, 6, 4, 6};
    straightInsertionSortInt(11, arr);

    for (int i = 0; i < 11; ++i) {
        printf("%d\t", arr[i]);
    }
    printf("\n");
}

int main() {
    straightInsertionSortTest();
    return 0;
}
