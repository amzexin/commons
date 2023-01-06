//
// Created by Vincent on 2021/5/9.
//

#ifndef INC_SINGLYLINKEDQUEUE_H
#define INC_SINGLYLINKEDQUEUE_H

#include <stdbool.h>

typedef struct singlyLinkedQueueNode {
    /**
     * 当前结点数据
     */
    void *dataPointer;
    /**
     * 下一个结点的指针
     */
    struct singlyLinkedQueueNode *next;
} SinglyLinkedQueueNode;

typedef struct {
    /**
     * 头结点
     */
    SinglyLinkedQueueNode *head;
    /**
     * 尾结点
     */
    SinglyLinkedQueueNode *tail;

    /**
     * 比较元素的函数指针
     */
    bool (*equalsElement)(void *, void *);

    /**
     * toString元素的函数指针
     */
    char *(*toStringElement)(void *);
} SinglyLinkedQueue;

/**
 * 打印队列所有内容
 *
 * @param singlyLinkedQueue 队列指针
 */
void singlyLinkedQueuePrintf(SinglyLinkedQueue *singlyLinkedQueue);

/**
 * 初始化一个队列
 *
 * @param equalsElement 比较元素的函数指针
 * @param toStringElement toString元素的函数指针
 * @return 队列指针
 */
SinglyLinkedQueue *singlyLinkedQueueInitiate(bool (*equalsElement)(void *, void *),
                                             char *(*toStringElement)(void *));

/**
 * 队列判空
 *
 * @param singlyLinkedQueue 队列指针
 * @return 是否为空
 */
bool singlyLinkedQueueIsEmpty(SinglyLinkedQueue *singlyLinkedQueue);

/**
 * 进队列
 *
 * @param singlyLinkedQueue 队列指针
 * @param dataPointer 要入队列的数据
 */
void singlyLinkedQueueEnElement(SinglyLinkedQueue *singlyLinkedQueue, void *dataPointer);

/**
 * 出队列
 *
 * @param singlyLinkedQueue 队列指针
 * @return 队列顶元素
 */
void *singlyLinkedQueueOutElement(SinglyLinkedQueue *singlyLinkedQueue);

/**
 * 取队列顶
 *
 * @param singlyLinkedQueue 队列指针
 * @return 队列顶元素
 */
void *singlyLinkedQueueGetHead(SinglyLinkedQueue *singlyLinkedQueue);

#endif  // INC_SINGLYLINKEDQUEUE_H
