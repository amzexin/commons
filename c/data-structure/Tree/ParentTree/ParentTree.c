//
// Created by 李泽鑫 on 2021/5/25.
//

#include "ParentTree.h"
#include <stdlib.h>

typedef char *string;

ParentTree *parentTreeInitiateForYH(ArrayList *allSubscribe) {
    ParentTree *parentTree = malloc(sizeof(ParentTree));
    ParentTreeNode *elementData = malloc(sizeof(ParentTreeNode) * allSubscribe->length);
    parentTree->elementData = elementData;
    parentTree->equalsElement = allSubscribe->equalsElement;

    ArrayListElement *elementArr = allSubscribe->elementArr;
    char *arr[] = {"ABCD", "BCDE", "CDEF"};
    for (int i = 0; i < 3; ++i) {
        char *str = arr[i];

    }

    return parentTree;
}

void parentTreeLevelPrintf(ParentTree *parentTree) {

}