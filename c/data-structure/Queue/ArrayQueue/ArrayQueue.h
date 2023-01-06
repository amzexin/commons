//
// Created by Vincent on 2021/5/16.
//

#ifndef DATA_STRUCTURE_ARRAYQUEUE_H
#define DATA_STRUCTURE_ARRAYQUEUE_H

#include <stdbool.h>

typedef struct {
    /**
     * 数据指针
     */
    void *dataPointer;
} ArrayQueueElement;

typedef struct {
    /**
     * 元素数组
     */
    ArrayQueueElement *elementArr;
    /**
     * 容量
     */
    int capacity;
    /**
     * 头索引，当前队列第一个元素的索引
     */
    int head;
    /**
     * 尾索引，新结点可以加入的位置
     */
    int tail;

    /**
     * 比较元素的函数指针
     */
    bool (*equalsElement)(void *, void *);

    /**
     * toString元素的函数指针
     */
    char *(*toStringElement)(void *);
} ArrayQueue;

/**
 * 打印队列
 *
 * @param arrayQueue 队列指针
 */
void arrayQueuePrintf(ArrayQueue *arrayQueue);

/**
 * 初始化一个指定容量的队列
 *
 * @param capcity 容量
 * @param equalsElement 比较元素的函数指针
 * @param toStringElement toString元素的函数指针
 * @return 队列指针
 */
ArrayQueue *arrayQueueInitiate(int capacity, bool (*equalsElement)(void *, void *),
                               char *(*toStringElement)(void *));

/**
 * 队列判空
 *
 * @param Queue 队列指针
 * @return 是否为空
 */
bool arrayQueueIsEmpty(ArrayQueue *arrayQueue);

/**
 * 进队列
 *
 * @param Queue 队列指针
 * @param dataPointer 要入队列的数据
 */
void arrayQueueEnElement(ArrayQueue *arrayQueue, void *dataPointer);

/**
 * 出队列
 *
 * @param arrayQueue 队列指针
 * @return 队列顶元素
 */
void *arrayQueueOutElement(ArrayQueue *arrayQueue);

/**
 * 取队列顶
 *
 * @param arrayQueue 队列指针
 * @return 队列顶元素
 */
void *arrayQueueGetHeadElement(ArrayQueue *arrayQueue);

#endif //DATA_STRUCTURE_ARRAYQUEUE_H
