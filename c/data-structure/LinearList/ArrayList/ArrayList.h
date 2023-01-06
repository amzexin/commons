//
// Created by Vincent on 2021/5/15.
//

#ifndef INC_ARRAYLIST_H
#define INC_ARRAYLIST_H

#include <stdbool.h>

typedef struct {
    /**
     * 真实数据指针
     */
    void *dataPointer;
} ArrayListElement;

typedef struct {
    /**
     * 所有元素；一维数组
     */
    ArrayListElement *elementArr;
    /**
     * 顺序表长度
     */
    int length;
    /**
     * 顺序表容量
     */
    int capacity;

    /**
     * 比较元素的函数指针
     */
    bool (*equalsElement)(void *, void *);

    /**
     * toString元素的函数指针
     */
    char *(*toStringElement)(void *);
} ArrayList;

/**
 * 打印顺序表
 *
 * @param seqList
 */
void arrayListPrintf(ArrayList *seqList);

/**
 * 初始化一个顺序表
 *
 * @param initCapacity 顺序表初始容量
 * @param equalsElement 比较元素的函数指针
 * @param toStringElement toString元素的函数指针
 * @return 顺序表
 */
ArrayList *arrayListInitiate(int initCapacity, bool (*equalsElement)(void *, void *),
                             char *(*toStringElement)(void *));

/**
 * 添加一个元素到顺序表指定的下标
 *
 * @param arrayList 顺序表指针
 * @param elementDataPointer 元素指针
 * @param index 顺序表索引，取值范围为[0, length(seqList)]
 */
void arrayListInsertElement(ArrayList *arrayList, void *elementDataPointer, int index);

/**
 * 为顺序表指定的下标设置元素
 *
 * @param arrayList 顺序表指针
 * @param elementDataPointer 元素指针
 * @param index 顺序表索引，取值范围为[0, length(seqList)]
 */
void arrayListSetElement(ArrayList *arrayList, void *elementDataPointer, int index);

/**
 * 添加元素到顺序表
 *
 * @param arrayList
 * @param elementDataPointer
 */
void arrayListAddElement(ArrayList *arrayList, void *elementDataPointer);

/**
 * 获取索引为i的元素
 *
 * @param arrayList 顺序表
 * @param index 元素索引
 * @return 索引为i的元素
 */
void *arrayListGetElement(ArrayList *arrayList, int index);

/**
 * 定位一个元素
 *
 * @param arrayList 顺序表
 * @param elementDataPointer 要定位的元素
 * @return 元素的索引
 */
int arrayListLocateElement(ArrayList *arrayList, void *elementDataPointer);

/**
 * 删除一个元素
 *
 * @param arrayList 顺序表
 * @param index 元素索引
 */
void arrayListDeleteElement(ArrayList *arrayList, int index);

#endif  // INC_ARRAYLIST_H
