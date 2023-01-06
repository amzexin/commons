//
// Created by Vincent on 2021/5/16.
//

#include "SinglyLinkedStack.h"

#include <stdio.h>
#include <stdlib.h>

void singlyLinkedStackPrintf(SinglyLinkedStack *singlyLinkedStack) {
    SinglyLinkedStackNode *head = singlyLinkedStack->head;
    if (head->next == NULL) {
        printf("SinglyLinkedStack is empty \n");
        return;
    }

    printf("栈顶 -> 栈底: ");
    SinglyLinkedStackNode *pLinkedStackNode = head->next;
    char *(*toStringElement)(void *) = singlyLinkedStack->toStringElement;
    while (pLinkedStackNode != NULL) {
        printf("%s\t", toStringElement(pLinkedStackNode->dataPointer));
        pLinkedStackNode = pLinkedStackNode->next;
    }
    printf("\n");
}

SinglyLinkedStack *singlyLinkedStackInitiate(bool (*equalsElement)(void *, void *),
                                             char *(*toStringElement)(void *)) {
    SinglyLinkedStack *singlyLinkedStack = malloc(sizeof(SinglyLinkedStack));

    SinglyLinkedStackNode *head = malloc(sizeof(SinglyLinkedStackNode));
    head->next = NULL;
    singlyLinkedStack->head = head;
    singlyLinkedStack->equalsElement = equalsElement;
    singlyLinkedStack->toStringElement = toStringElement;

    return singlyLinkedStack;
}

int singlyLinkedStackIsEmpty(SinglyLinkedStack *singlyLinkedStack) { return singlyLinkedStack->head->next == NULL; }

void singlyLinkedStackPush(SinglyLinkedStack *singlyLinkedStack, void *dataPointer) {
    SinglyLinkedStackNode *head = singlyLinkedStack->head;

    SinglyLinkedStackNode *curNode = malloc(sizeof(SinglyLinkedStackNode));
    curNode->dataPointer = dataPointer;

    curNode->next = head->next;
    head->next = curNode;
}

void *singlyLinkedStackPop(SinglyLinkedStack *singlyLinkedStack) {
    if (singlyLinkedStackIsEmpty(singlyLinkedStack)) {
        printf("栈已空 \n");
        exit(1);
    }

    SinglyLinkedStackNode *topNode = singlyLinkedStack->head->next;
    singlyLinkedStack->head->next = topNode->next;

    void *dataPointer = topNode->dataPointer;
    free(topNode);

    return dataPointer;
}

void *singlyLinkedStackGetTop(SinglyLinkedStack *singlyLinkedStack) {
    if (singlyLinkedStackIsEmpty(singlyLinkedStack)) {
        printf("栈已空 \n");
        exit(1);
    }

    SinglyLinkedStackNode *topNode = singlyLinkedStack->head->next;
    return topNode->dataPointer;
}
