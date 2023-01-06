//
// Created by Vincent on 2021/5/16.
//

#ifndef DATA_STRUCTURE_LINKBINARYTREE_H
#define DATA_STRUCTURE_LINKBINARYTREE_H

#include <stdbool.h>

#include "SinglyLinkedList.h"

/**
 * 二叉树结点
 */
typedef struct linkBinaryTreeNode {
    /**
     * 当前结点数据指针
     */
    void *dataPointer;
    /**
     * 左右孩子
     */
    struct linkBinaryTreeNode *lchild, *rchild;
} LinkBinaryTreeNode;

/**
 * 二叉树
 */
typedef struct {
    /**
     * 二叉树的根节点
     */
    LinkBinaryTreeNode *root;

    /**
     * 比较元素的函数指针
     */
    bool (*equalsElement)(void *, void *);

    /**
     * toString元素的函数指针
     */
    char *(*toStringElement)(void *);

} LinkBinaryTree;

/**
 * 二叉树遍历类型
 */
typedef enum {
    /**
     * 前序遍历
     */
    preorder = 0,
    /**
     * 中序遍历
     */
    inorder = 1,
    /**
     * 后序遍历
     */
    postorder = 2
} AdjacencyMatrixGraphTraversalType;

/**
 * 初始化二叉树
 *
 * @param rootDataPointer 根数据指针
 * @param equalsElement 比较元素的函数指针
 * @param toStringElement toString元素的函数指针
 * @return 二叉树指针
 */
LinkBinaryTree *linkBinaryTreeInitiate(void *rootDataPointer, bool (*equalsElement)(void *, void *),
                                       char *(*toStringElement)(void *));

/**
 * 为父节点设置左孩子
 *
 * @param linkBinaryTree 二叉树指针
 * @param parentDataPointer 父节点
 * @param childDataPointer 孩子结点
 */
void linkBinaryTreeSetLeftChild(LinkBinaryTree *linkBinaryTree, void *parentDataPointer, void *childDataPointer);

/**
 * 为父节点设置右孩子
 *
 * @param linkBinaryTree 二叉树指针
 * @param parentDataPointer 父节点
 * @param childDataPointer 孩子结点
 */
void linkBinaryTreeSetRightChild(LinkBinaryTree *linkBinaryTree, void *parentDataPointer, void *childDataPointer);

/**
 * 遍历root并将遍历结果返回
 *
 * @param linkBinaryTree 二叉树指针
 * @param type 遍历类型
 * @return 遍历结果
 */
SinglyLinkedList *
linkBinaryTreeTraversalGetData(LinkBinaryTree *linkBinaryTree, AdjacencyMatrixGraphTraversalType type);

/**
 * 遍历交换左右孩子结点
 *
 * @param linkBinaryTree 二叉树指针
 * @param type 遍历类型
 */
void linkBinaryTreeTraversalSwapChild(LinkBinaryTree *linkBinaryTree, AdjacencyMatrixGraphTraversalType type);

#endif //DATA_STRUCTURE_LINKBINARYTREE_H
