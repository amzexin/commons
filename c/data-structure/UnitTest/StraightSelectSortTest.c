//
// Created by 李泽鑫 on 2021/5/25.
//

#include "StraightSelectSort.h"
#include "CommonUtil.h"
#include <stdio.h>

void straightSelectSortTest() {

    int arrLength = 11;
    int arr[] = {1, 5, 9, 3, 4, 5, 8, 6, 6, 4, 6};
    ascSorted(arrLength, arr);

    straightSelectSortInt(arrLength, arr);
    ascSorted(arrLength, arr);

    arrPrintf(arrLength, arr);
}

int main() {
    straightSelectSortTest();
    return 0;
}
