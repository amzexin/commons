//
// Created by Vincent on 2021/5/16.
//

#ifndef DATA_STRUCTURE_ARRAYSTACK_H
#define DATA_STRUCTURE_ARRAYSTACK_H

#include <stdbool.h>

#define EMPTY_STACK_TOP -1

typedef struct {
    /**
     * 数据指针
     */
    void *dataPointer;
} ArrayStackElement;

typedef struct {
    /**
     * 元素数组
     */
    ArrayStackElement *elementArr;
    /**
     * 栈容量
     */
    int capacity;
    /**
     * 栈顶索引
     */
    int top;

    /**
     * 比较元素的函数指针
     */
    bool (*equalsElement)(void *, void *);

    /**
     * toString元素的函数指针
     */
    char *(*toStringElement)(void *);
} ArrayStack;

/**
 * 打印顺序栈
 *
 * @param arrayStack
 */
void arrayStackPrintf(ArrayStack *arrayStack);

/**
 * 初始化一个指定容量的栈
 *
 * @param capcity 容量
 * @return 栈指针
 */
ArrayStack *arrayStackInitiate(int capacity, bool (*equalsElement)(void *, void *),
                               char *(*toStringElement)(void *));

/**
 * 栈判空
 *
 * @param arrayStack 栈指针
 * @return 是否为空
 */
bool arrayStackIsEmpty(ArrayStack *arrayStack);

/**
 * 进栈
 *
 * @param arrayStack 栈指针
 * @param dataPointer 要入栈的数据指针
 */
void arrayStackPush(ArrayStack *arrayStack, void *dataPointer);

/**
 * 出栈
 *
 * @param arrayStack 栈指针
 * @return 栈顶元素
 */
void *arrayStackPop(ArrayStack *arrayStack);

/**
 * 取栈顶
 *
 * @param arrayStack 栈指针
 * @return 栈顶元素
 */
void *arrayStackGetTop(ArrayStack *arrayStack);

#endif //DATA_STRUCTURE_ARRAYSTACK_H
