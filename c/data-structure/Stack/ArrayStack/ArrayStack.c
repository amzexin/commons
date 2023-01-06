//
// Created by Vincent on 2021/5/16.
//

#include "ArrayStack.h"

#include <stdio.h>
#include <stdlib.h>

void arrayStackPrintf(ArrayStack *arrayStack) {
    if (arrayStackIsEmpty(arrayStack)) {
        printf("SeqStack is empty \n");
        return;
    }

    printf("栈底 -> 栈顶：\n");
    ArrayStackElement *elementArr = arrayStack->elementArr;
    char *(*toStringElement)(void *) = arrayStack->toStringElement;
    int top = arrayStack->top;
    for (int i = 0; i <= top; i++) {
        printf("%s\t", toStringElement(elementArr[i].dataPointer));
    }
    printf("\n");
}

ArrayStack *arrayStackInitiate(int capacity, bool (*equalsElement)(void *, void *),
                               char *(*toStringElement)(void *)) {
    if (capacity < 0) {
        printf("Illegal Capacity: %d", capacity);
        exit(1);
    }

    ArrayStack *arrayStack = malloc(sizeof(ArrayStack));
    arrayStack->elementArr = malloc(sizeof(ArrayStackElement) * capacity);
    arrayStack->capacity = capacity;
    arrayStack->top = EMPTY_STACK_TOP;
    arrayStack->equalsElement = equalsElement;
    arrayStack->toStringElement = toStringElement;

    return arrayStack;
}

bool arrayStackIsEmpty(ArrayStack *arrayStack) { return arrayStack->top == EMPTY_STACK_TOP; }

void arrayStackPush(ArrayStack *arrayStack, void *dataPointer) {
    int top = arrayStack->top;
    int capacity = arrayStack->capacity;
    if (top == capacity - 1) {
        printf("栈已满 \n");
        exit(1);
    }

    ArrayStackElement *elementArr = arrayStack->elementArr;
    elementArr[++(arrayStack->top)].dataPointer = dataPointer;
}

void *arrayStackPop(ArrayStack *arrayStack) {
    if (arrayStackIsEmpty(arrayStack)) {
        printf("栈已空 \n");
        exit(1);
    }

    ArrayStackElement *elementArr = arrayStack->elementArr;
    return elementArr[arrayStack->top--].dataPointer;
}

void *arrayStackGetTop(ArrayStack *arrayStack) {
    if (arrayStackIsEmpty(arrayStack)) {
        printf("栈已空 \n");
        exit(1);
    }

    ArrayStackElement *elementArr = arrayStack->elementArr;
    return elementArr[arrayStack->top].dataPointer;
}