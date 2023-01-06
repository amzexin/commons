//
// Created by Vincent on 2021/5/15.
//

#include "ArrayList.h"

#include <stdio.h>
#include <stdlib.h>

void arrayListPrintf(ArrayList *seqList) {
    int length = seqList->length;
    if (length == 0) {
        printf("seqList is empty\n");
        return;
    }

    ArrayListElement *elementData = seqList->elementArr;
    char *(*toStringElement)(void *) = seqList->toStringElement;
    for (int i = 0; i < length; i++) {
        printf("%s\n", toStringElement(elementData[i].dataPointer));
    }
}

ArrayList *arrayListInitiate(int initCapacity,
                             bool (*equalsElement)(void *, void *),
                             char *(*toStringElement)(void *)) {
    if (initCapacity < 0) {
        printf("Illegal Capacity: %d", initCapacity);
        exit(1);
    }
    ArrayList *seqList = malloc(sizeof(ArrayList));
    seqList->elementArr = malloc(sizeof(ArrayListElement) * initCapacity);
    seqList->capacity = initCapacity;
    seqList->length = 0;
    seqList->equalsElement = equalsElement;
    seqList->toStringElement = toStringElement;
    return seqList;
}

void arrayListResize(ArrayList *arrayList) {
    int oldCapacity = arrayList->capacity;
    int newCapacity = oldCapacity * 2;
    printf("开始从%d扩容到%d\n", oldCapacity, newCapacity);
    arrayList->capacity = newCapacity;

    ArrayListElement *oldElementData = arrayList->elementArr;
    ArrayListElement *newElementData =
            malloc(sizeof(ArrayListElement) * newCapacity);
    arrayList->elementArr = newElementData;

    for (int i = 0; i < oldCapacity; ++i) {
        newElementData[i] = oldElementData[i];
    }
}

void arrayListInsertElement(ArrayList *arrayList, void *elementDataPointer,
                            int index) {
    int length = arrayList->length;
    if (index < 0) {
        printf("arrayListInsertElement index[%d] < 0 \n", index);
        exit(1);
    }

    // 扩容
    int capacity = arrayList->capacity;
    if (length == capacity) {
        arrayListResize(arrayList);
        length = arrayList->length;
    }

    ArrayListElement *elementData = arrayList->elementArr;
    for (int j = length; j > index; j--) {
        elementData[j] = elementData[j - 1];
    }

    ArrayListElement newElement;
    newElement.dataPointer = elementDataPointer;
    elementData[index] = newElement;
    arrayList->length++;
}

void arrayListSetElement(ArrayList *arrayList, void *elementDataPointer, int index) {
    int length = arrayList->length;
    if (index < 0 || index > length - 1) {
        printf("arrayListSetElement 非法index[%d], 当前length[%d] \n", index, length);
        exit(1);
    }

    ArrayListElement *elementData = arrayList->elementArr;
    elementData[index].dataPointer = elementDataPointer;
}

void arrayListAddElement(ArrayList *arrayList, void *elementDataPointer) {
    arrayListInsertElement(arrayList, elementDataPointer, arrayList->length);
}

void *arrayListGetElement(ArrayList *arrayList, int index) {
    int length = arrayList->length;
    if (index < 0 || index > length - 1) {
        printf("非法index[%d], 当前length[%d] \n", index, length);
        exit(1);
    }

    ArrayListElement *elementData = arrayList->elementArr;
    return elementData[index].dataPointer;
}

int arrayListLocateElement(ArrayList *arrayList, void *elementDataPointer) {
    int length = arrayList->length;
    ArrayListElement *elementData = arrayList->elementArr;
    bool (*equalsElement)(void *, void *) = arrayList->equalsElement;
    for (int i = 0; i < length; i++) {
        if (equalsElement(elementData[i].dataPointer, elementDataPointer)) {
            return i;
        }
    }
    return -1;
}

void arrayListDeleteElement(ArrayList *arrayList, int index) {
    int length = arrayList->length;
    if (index < 0 || index > length - 1) {
        printf("非法index[%d], 当前length[%d] \n", index, length);
        exit(1);
    }

    ArrayListElement *elementData = arrayList->elementArr;
    for (int i = index; i < length - 2; i++) {
        elementData[i] = elementData[i + 1];
    }

    arrayList->length--;
}
