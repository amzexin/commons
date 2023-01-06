//
// Created by 李泽鑫 on 2021/5/25.
//

#ifndef PARENTTREE_H
#define PARENTTREE_H

#include <stdbool.h>
#include "ArrayList.h"

/**
 * 双亲树结点信息
 */
typedef struct {
    /**
     * 当前结点的数据指针
     */
    void *dataPointer;
    /**
     * 当前节点双亲节点的索引
     */
    int parentIndex;
} ParentTreeNode;

/**
 * 双亲表示法的树
 */
typedef struct {
    /**
     * 树的所有结点
     */
    ParentTreeNode *elementData;
    /**
     * 比较元素
     */
    bool (*equalsElement)(void *, void *);
} ParentTree;

/**
 * 初始化双亲树
 *
 * @param allSubscribe 所有的订阅信息
 * @return 双亲树
 */
ParentTree *parentTreeInitiateForYH(ArrayList *allSubscribe);

/**
 * 双亲树层级关系打印
 *
 * @param parentTree 双亲树
 */
void parentTreeLevelPrintf(ParentTree *parentTree);

#endif //PARENTTREE_H
