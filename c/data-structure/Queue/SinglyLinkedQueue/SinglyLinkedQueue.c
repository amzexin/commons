//
// Created by Vincent on 2021/5/9.
//
#include "SinglyLinkedQueue.h"

#include <stdio.h>
#include <stdlib.h>

void singlyLinkedQueuePrintf(SinglyLinkedQueue *singlyLinkedQueue) {
    if (singlyLinkedQueue->head == singlyLinkedQueue->tail) {
        printf("Queue is empty \n");
        return;
    }

    printf("queue content is:");
    char *(*toStringElement)(void *) = singlyLinkedQueue->toStringElement;
    SinglyLinkedQueueNode *node = singlyLinkedQueue->head->next;
    while (node != NULL) {
        printf("%s\t", toStringElement(node->dataPointer));
        node = node->next;
    }
    printf("\n");
}

SinglyLinkedQueue *singlyLinkedQueueInitiate(bool (*equalsElement)(void *, void *),
                                             char *(*toStringElement)(void *)) {
    SinglyLinkedQueue *linkQueue = malloc(sizeof(SinglyLinkedQueue));
    linkQueue->equalsElement = equalsElement;
    linkQueue->toStringElement = toStringElement;

    SinglyLinkedQueueNode *head = malloc(sizeof(SinglyLinkedQueueNode));
    head->next = NULL;
    linkQueue->head = head;
    linkQueue->tail = head;

    return linkQueue;
}

bool singlyLinkedQueueIsEmpty(SinglyLinkedQueue *singlyLinkedQueue) {
    return singlyLinkedQueue->head == singlyLinkedQueue->tail;
}

void singlyLinkedQueueEnElement(SinglyLinkedQueue *singlyLinkedQueue, void *dataPointer) {
    SinglyLinkedQueueNode *curNode = malloc(sizeof(SinglyLinkedQueueNode));
    curNode->dataPointer = dataPointer;
    curNode->next = NULL;

    SinglyLinkedQueueNode *tail = singlyLinkedQueue->tail;
    tail->next = curNode;
    singlyLinkedQueue->tail = curNode;
}

void *singlyLinkedQueueOutElement(SinglyLinkedQueue *singlyLinkedQueue) {
    if (singlyLinkedQueueIsEmpty(singlyLinkedQueue)) {
        printf("队列已空 \n");
        exit(1);
    }

    SinglyLinkedQueueNode *head = singlyLinkedQueue->head;
    SinglyLinkedQueueNode *firstNode = head->next;
    head->next = firstNode->next;

    if (firstNode->next == NULL) {
        singlyLinkedQueue->tail = head;
    }

    void *firstData = firstNode->dataPointer;
    free(firstNode);

    return firstData;
}

void *singlyLinkedQueueGetHead(SinglyLinkedQueue *singlyLinkedQueue) {
    if (singlyLinkedQueueIsEmpty(singlyLinkedQueue)) {
        printf("队列已空 \n");
        exit(1);
    }

    SinglyLinkedQueueNode *head = singlyLinkedQueue->head;
    SinglyLinkedQueueNode *firstNode = head->next;

    return firstNode->dataPointer;
}