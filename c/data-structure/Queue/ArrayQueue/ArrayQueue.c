//
// Created by Vincent on 2021/5/16.
//

#include "ArrayQueue.h"

#include <stdio.h>
#include <stdlib.h>

void arrayQueuePrintf(ArrayQueue *arrayQueue) {
    int headIndex = arrayQueue->head;
    int tailIndex = arrayQueue->tail;

    if (headIndex == tailIndex) {
        printf("SeqQueue is empty \n");
        return;
    }

    printf("queue content is: ");
    ArrayQueueElement *elementArr = arrayQueue->elementArr;
    char *(*toStringElement)(void *) =arrayQueue->toStringElement;
    int capacity = arrayQueue->capacity;
    for (int i = headIndex; i != tailIndex; i++) {
        i %= capacity;
        printf("%s\t", toStringElement(elementArr[i].dataPointer));
    }
    printf("\n");
}

ArrayQueue *arrayQueueInitiate(int capacity, bool (*equalsElement)(void *, void *),
                               char *(*toStringElement)(void *)) {
    if (capacity < 0) {
        printf("Illegal Capacity: %d", capacity);
        exit(1);
    }

    ArrayQueue *queue = malloc(sizeof(ArrayQueue));
    queue->elementArr = malloc(sizeof(ArrayQueueElement) * capacity);
    queue->capacity = capacity;
    queue->head = 0;
    queue->tail = 0;
    queue->equalsElement = equalsElement;
    queue->toStringElement = toStringElement;

    return queue;
}

bool arrayQueueIsEmpty(ArrayQueue *arrayQueue) { return arrayQueue->head == arrayQueue->tail; }

void arrayQueueEnElement(ArrayQueue *arrayQueue, void *dataPointer) {
    int headIndex = arrayQueue->head;
    int tailIndex = arrayQueue->tail;
    int capacity = arrayQueue->capacity;
    if ((tailIndex + 1) % capacity == headIndex) {
        printf("队列已满 \n");
        exit(1);
    }

    ArrayQueueElement *elementArr = arrayQueue->elementArr;
    elementArr[(arrayQueue->tail)++].dataPointer = dataPointer;
}

void *arrayQueueOutElement(ArrayQueue *arrayQueue) {
    if (arrayQueueIsEmpty(arrayQueue)) {
        printf("队列已空 \n");
        exit(1);
    }

    ArrayQueueElement *elementArr = arrayQueue->elementArr;
    ArrayQueueElement headElement = elementArr[arrayQueue->head];
    arrayQueue->head = (arrayQueue->head + 1) % arrayQueue->capacity;

    return headElement.dataPointer;
}

void *arrayQueueGetHeadElement(ArrayQueue *arrayQueue) {
    if (arrayQueueIsEmpty(arrayQueue)) {
        printf("队列已空 \n");
        exit(1);
    }

    ArrayQueueElement *elementArr = arrayQueue->elementArr;
    return elementArr[arrayQueue->head].dataPointer;
}