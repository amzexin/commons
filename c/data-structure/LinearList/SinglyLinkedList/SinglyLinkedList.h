//
// Created by Vincent on 2021/5/11.
//

#ifndef INC_SINGLYLINKEDLIST_H
#define INC_SINGLYLINKEDLIST_H

#include <stdbool.h>

typedef struct singlyLinkedListNode {
    /**
     * 数据
     */
    void *dataPointer;
    /**
     * 下一个结点指针
     */
    struct singlyLinkedListNode *next;
} SinglyLinkedListNode;

typedef struct {
    /**
     * 头结点
     */
    SinglyLinkedListNode *head;
    /**
     * 单链表长度
     */
    int length;

    /**
     * 比较元素的函数指针
     */
    bool (*equalsElement)(void *, void *);

    /**
     * toString元素的函数指针
     */
    char *(*toStringElement)(void *);
} SinglyLinkedList;

/**
 * 打印链表
 *
 * @param singlyLinkedList 单链表指针
 */
void singlyLinkedListPrintf(SinglyLinkedList *singlyLinkedList);

/**
 * 初始化一个单链表
 *
 * @param equalsElement 比较元素的函数指针
 * @param toStringElement toString元素的函数指针
 * @return 单链表
 */
SinglyLinkedList *singlyLinkedListInitiate(bool (*equalsElement)(void *, void *), char *(*toStringElement)(void *));

/**
 * 添加一个元素到单链表指定的下标
 *
 * @param singlyLinkedList 单链表指针
 * @param elementDataPointer 元素指针
 * @param index 单链表索引，取值范围为[0, length(seqList)]
 */
void singlyLinkedListInsertElement(SinglyLinkedList *singlyLinkedList, void *elementDataPointer, int index);

/**
 * 添加元素到单链表
 *
 * @param singlyLinkedList 单链表指针
 * @param elementDataPointer 元素指针
 */
void singlyLinkedListAddElement(SinglyLinkedList *singlyLinkedList, void *elementDataPointer);

/**
 * 获取索引为i的元素
 *
 * @param singlyLinkedList 单链表指针
 * @param index 元素索引
 * @return 索引为i的元素
 */
void *singlyLinkedListGetElement(SinglyLinkedList *singlyLinkedList, int index);

/**
 * 定位一个元素
 *
 * @param singlyLinkedList 单链表指针
 * @param elementDataPointer 要查找的元素的指针
 * @return 元素的索引
 */
int singlyLinkedListLocateElement(SinglyLinkedList *singlyLinkedList, void *elementDataPointer);

/**
 * 删除一个元素
 *
 * @param singlyLinkedList 单链表指针
 * @param index 元素索引
 */
void singlyLinkedListDeleteElement(SinglyLinkedList *singlyLinkedList, int index);

/**
 * 单链表空判断
 *
 * @param singlyLinkedList
 * @return
 */
bool singlyLinkedListIsEmpty(SinglyLinkedList *singlyLinkedList);

#endif  // INC_SINGLYLINKEDLIST_H
