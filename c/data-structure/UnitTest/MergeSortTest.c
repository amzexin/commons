//
// Created by Vincent on 2021/5/30.
//

#include "MergeSort.h"
#include "CommonUtil.h"

void mergeSortForIntTest() {

    int arrLength = 11;
    int arr[] = {1, 5, 9, 3, 4, 5, 8, 6, 6, 4, 6};
    ascSorted(arrLength, arr);

    mergeSortForInt(arrLength, arr);
    ascSorted(arrLength, arr);

    arrPrintf(arrLength, arr);
}

int main() {
    mergeSortForIntTest();
    return 0;
}

