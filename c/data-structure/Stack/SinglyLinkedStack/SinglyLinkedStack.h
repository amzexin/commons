//
// Created by Vincent on 2021/5/16.
//

#ifndef DATA_STRUCTURE_SINGLYLINKEDSTACK_H
#define DATA_STRUCTURE_SINGLYLINKEDSTACK_H

#include <stdbool.h>

typedef struct singlyLinkedStackNode {
    /**
     * 结点数据指针
     */
    void *dataPointer;
    /**
     * 下一个结点指针
     */
    struct singlyLinkedStackNode *next;
} SinglyLinkedStackNode;

typedef struct {
    /**
     * 头结点指针
     */
    SinglyLinkedStackNode *head;

    /**
     * 比较元素的函数指针
     */
    bool (*equalsElement)(void *, void *);

    /**
     * toString元素的函数指针
     */
    char *(*toStringElement)(void *);

} SinglyLinkedStack;

/**
 * 打印栈
 *
 * @param singlyLinkedStack
 */
void singlyLinkedStackPrintf(SinglyLinkedStack *singlyLinkedStack);

/**
 * 初始化一个栈
 *
 * @return 栈指针
 */
SinglyLinkedStack *singlyLinkedStackInitiate(bool (*equalsElement)(void *, void *),
                                             char *(*toStringElement)(void *));

/**
 * 栈判空
 *
 * @param singlyLinkedStack 栈指针
 * @return 是否为空
 */
int singlyLinkedStackIsEmpty(SinglyLinkedStack *singlyLinkedStack);

/**
 * 进栈
 *
 * @param singlyLinkedStack 栈指针
 * @param dataPointer 要入栈的数据指针
 */
void singlyLinkedStackPush(SinglyLinkedStack *singlyLinkedStack, void *dataPointer);

/**
 * 出栈
 *
 * @param singlyLinkedStack 栈指针
 * @return 栈顶元素
 */
void *singlyLinkedStackPop(SinglyLinkedStack *singlyLinkedStack);

/**
 * 取栈顶
 *
 * @param singlyLinkedStack 栈指针
 * @return 栈顶元素
 */
void *singlyLinkedStackGetTop(SinglyLinkedStack *singlyLinkedStack);

#endif //DATA_STRUCTURE_SINGLYLINKEDSTACK_H
