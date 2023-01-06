//
// Created by Vincent on 2021/5/16.
//

#include "LinkBinaryTree.h"

#include <stdlib.h>
#include <stdio.h>

LinkBinaryTree *linkBinaryTreeInitiate(void *rootDataPointer, bool (*equalsElement)(void *, void *),
                                       char *(*toStringElement)(void *)) {

    LinkBinaryTree *linkBinaryTree = malloc(sizeof(LinkBinaryTree));
    LinkBinaryTreeNode *root = malloc(sizeof(LinkBinaryTreeNode));
    root->dataPointer = rootDataPointer;
    root->lchild = NULL;
    root->rchild = NULL;
    linkBinaryTree->root = root;

    linkBinaryTree->equalsElement = equalsElement;
    linkBinaryTree->toStringElement = toStringElement;
    return linkBinaryTree;
}

LinkBinaryTreeNode *traversalGetNode(LinkBinaryTreeNode *currentNode, void *dataPointer,
                                     bool (*equalsElement)(void *, void *)) {
    if (currentNode == NULL) {
        return NULL;
    }

    if (equalsElement(currentNode->dataPointer, dataPointer)) {
        return currentNode;
    }

    LinkBinaryTreeNode *dataNode = traversalGetNode(currentNode->lchild, dataPointer, equalsElement);
    if (dataNode != NULL) {
        return dataNode;
    }

    dataNode = traversalGetNode(currentNode->rchild, dataPointer, equalsElement);
    if (dataNode != NULL) {
        return dataNode;
    }

    return NULL;
}

void linkBinaryTreeSetLeftChild(LinkBinaryTree *linkBinaryTree, void *parentDataPointer, void *childDataPointer) {
    LinkBinaryTreeNode *parentNode = traversalGetNode(linkBinaryTree->root, parentDataPointer,
                                                      linkBinaryTree->equalsElement);
    if (parentNode == NULL) {
        printf("linkBinaryTreeSetLeftChild失败，没有找到父节点，父节点为：%s", linkBinaryTree->toStringElement(parentDataPointer));
        exit(1);
    }

    LinkBinaryTreeNode *childNode = parentNode->lchild;
    if (childNode == NULL) {
        childNode = malloc(sizeof(LinkBinaryTreeNode));
        parentNode->lchild = childNode;
    }

    childNode->dataPointer = childDataPointer;

}

void linkBinaryTreeSetRightChild(LinkBinaryTree *linkBinaryTree, void *parentDataPointer, void *childDataPointer) {
    LinkBinaryTreeNode *parentNode = traversalGetNode(linkBinaryTree->root, parentDataPointer,
                                                      linkBinaryTree->equalsElement);
    if (parentNode == NULL) {
        printf("linkBinaryTreeSetRightChild失败，没有找到父节点，父节点为：%s", linkBinaryTree->toStringElement(parentDataPointer));
        exit(1);
    }

    LinkBinaryTreeNode *childNode = parentNode->rchild;
    if (childNode == NULL) {
        childNode = malloc(sizeof(LinkBinaryTreeNode));
        parentNode->rchild = childNode;
    }

    childNode->dataPointer = childDataPointer;

}

void
traversalGetData(LinkBinaryTreeNode *root, AdjacencyMatrixGraphTraversalType type, SinglyLinkedList *singlyLinkedList) {
    if (root == NULL) {
        return;
    }
    switch (type) {
        case preorder:
            singlyLinkedListAddElement(singlyLinkedList, root->dataPointer);
            traversalGetData(root->lchild, preorder, singlyLinkedList);
            traversalGetData(root->rchild, preorder, singlyLinkedList);
            break;
        case inorder:
            traversalGetData(root->lchild, inorder, singlyLinkedList);
            singlyLinkedListAddElement(singlyLinkedList, root->dataPointer);
            traversalGetData(root->rchild, inorder, singlyLinkedList);
            break;
        case postorder:
            traversalGetData(root->lchild, postorder, singlyLinkedList);
            traversalGetData(root->rchild, postorder, singlyLinkedList);
            singlyLinkedListAddElement(singlyLinkedList, root->dataPointer);
            break;
        default:
            break;
    }
}

SinglyLinkedList *
linkBinaryTreeTraversalGetData(LinkBinaryTree *linkBinaryTree, AdjacencyMatrixGraphTraversalType type) {
    if (linkBinaryTree == NULL || linkBinaryTree->root == NULL) {
        return NULL;
    }

    // 初始化LinkList用于保存结果
    SinglyLinkedList *traversalResult = singlyLinkedListInitiate(linkBinaryTree->equalsElement,
                                                                 linkBinaryTree->toStringElement);

    // 开始遍历
    traversalGetData(linkBinaryTree->root, type, traversalResult);

    return traversalResult;
}

/**
 * 交换左右孩子结点
 *
 * @param linkBinaryTreeNode 二叉树结点指针
 */
void swapChild(LinkBinaryTreeNode *linkBinaryTreeNode) {
    LinkBinaryTreeNode *temp = linkBinaryTreeNode->lchild;
    linkBinaryTreeNode->lchild = linkBinaryTreeNode->rchild;
    linkBinaryTreeNode->rchild = temp;
}

/**
 * 遍历交换左右孩子结点
 *
 * @param root 根节点
 * @param type 遍历类型
 */
void traversalSwapChild(LinkBinaryTreeNode *root, AdjacencyMatrixGraphTraversalType type) {
    if (root == NULL) {
        return;
    }
    switch (type) {
        case preorder:
            swapChild(root);
            traversalSwapChild(root->lchild, preorder);
            traversalSwapChild(root->rchild, preorder);
            break;
        case inorder:
            traversalSwapChild(root->lchild, inorder);
            swapChild(root);
            traversalSwapChild(root->lchild, inorder);
            break;
        case postorder:
            traversalSwapChild(root->lchild, postorder);
            traversalSwapChild(root->rchild, postorder);
            swapChild(root);
            break;
        default:
            break;
    }
}

void linkBinaryTreeTraversalSwapChild(LinkBinaryTree *linkBinaryTree, AdjacencyMatrixGraphTraversalType type) {
    if (linkBinaryTree == NULL) {
        return;
    }

    traversalSwapChild(linkBinaryTree->root, type);
}
