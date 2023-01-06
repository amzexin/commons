//
// Created by Vincent on 2021/5/11.
//
#include "SinglyLinkedList.h"

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void singlyLinkedListPrintf(SinglyLinkedList *singlyLinkedList) {
    int length = singlyLinkedList->length;
    if (length == 0) {
        printf("linkList is empty\n");
        return;
    }

    char *(*toStringElement)(void *) = singlyLinkedList->toStringElement;
    SinglyLinkedListNode *node = singlyLinkedList->head->next;
    while (node != NULL) {
        printf("%s\t", toStringElement(node->dataPointer));
        node = node->next;
    }
    printf("\n");
}

SinglyLinkedList *singlyLinkedListInitiate(bool (*equalsElement)(void *, void *), char *(*toStringElement)(void *)) {
    SinglyLinkedList *singlyLinkedList = malloc(sizeof(SinglyLinkedList));
    singlyLinkedList->length = 0;

    SinglyLinkedListNode *head = malloc(sizeof(SinglyLinkedListNode));
    head->next = NULL;
    singlyLinkedList->head = head;

    singlyLinkedList->equalsElement = equalsElement;
    singlyLinkedList->toStringElement = toStringElement;

    return singlyLinkedList;
}

void singlyLinkedListInsertElement(SinglyLinkedList *singlyLinkedList, void *elementDataPointer, int index) {
    if (index < 0) {
        printf("非法index[%d] < 0 \n", index);
        exit(1);
    }

    SinglyLinkedListNode *preNode = singlyLinkedList->head;
    for (int i = 1; i <= index; i++) {
        preNode = preNode->next;
    }

    SinglyLinkedListNode *curNode = malloc(sizeof(SinglyLinkedListNode));
    curNode->dataPointer = elementDataPointer;

    curNode->next = preNode->next;
    preNode->next = curNode;

    singlyLinkedList->length++;
}

void singlyLinkedListAddElement(SinglyLinkedList *singlyLinkedList, void *elementDataPointer) {
    singlyLinkedListInsertElement(singlyLinkedList, elementDataPointer, singlyLinkedList->length);
}

void *singlyLinkedListGetElement(SinglyLinkedList *singlyLinkedList, int index) {
    int length = singlyLinkedList->length;
    if (index < 0 || index > length - 1) {
        printf("非法index[%d], 当前length[%d] \n", index, length);
        exit(1);
    }

    SinglyLinkedListNode *node = singlyLinkedList->head;
    for (int i = 0; i <= index; i++) {
        node = node->next;
    }

    return node->dataPointer;
}

int singlyLinkedListLocateElement(SinglyLinkedList *singlyLinkedList, void *elementDataPointer) {
    bool (*equalsElement)(void *, void *) = singlyLinkedList->equalsElement;
    SinglyLinkedListNode *node = singlyLinkedList->head->next;
    int index = 0;
    while (node == NULL ||
           equalsElement(node->dataPointer, elementDataPointer) != 0) {
        node = node->next;
        index++;
    }

    return node == NULL ? -1 : index;
}

void singlyLinkedListDeleteElement(SinglyLinkedList *singlyLinkedList, int index) {
    int length = singlyLinkedList->length;
    if (index < 0 || index > length - 1) {
        printf("非法index[%d], 当前length[%d] \n", index, length);
        exit(1);
    }

    SinglyLinkedListNode *preNode = singlyLinkedList->head;
    for (int i = 0; i < index - 1; i++) {
        preNode = preNode->next;
    }

    SinglyLinkedListNode *curNode = preNode->next;
    preNode->next = curNode->next;
    free(curNode);

    singlyLinkedList->length--;
}

bool singlyLinkedListIsEmpty(SinglyLinkedList *singlyLinkedList) {
    return singlyLinkedList->length == 0;
}