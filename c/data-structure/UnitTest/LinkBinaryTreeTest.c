#include <stdio.h>
#include <stdlib.h>

#include "LinkBinaryTree.h"
#include "CommonUtil.h"

/**
 * 构建如下图的二叉树，用于后续测试
 *     A
 *    /
 *   B
 *  / \
 * C   D
 *    / \
 *   E   F
 *    \
 *     G
 * 前序遍历：ABCDEGF
 * 中序遍历：CBEGDFA
 * 后序遍历：CGEFDBA
 *
 * 左右孩子结点交换之后的二叉树
 *   A
 *    \
 *     B
 *    / \
 *   D   C
 *  / \
 * F   E
 *    /
 *   G
 * 前序遍历：ABDFEGC
 * 中序遍历：AFDGEBC
 * 后序遍历：FGEDCBA
 */
LinkBinaryTree *createBinaryTree() {
    LinkBinaryTree *linkBinaryTree = linkBinaryTreeInitiate("A", equalsString, toStringString);
    linkBinaryTreeSetLeftChild(linkBinaryTree, "A", "B");
    linkBinaryTreeSetLeftChild(linkBinaryTree, "B", "C");
    linkBinaryTreeSetRightChild(linkBinaryTree, "B", "D");
    linkBinaryTreeSetLeftChild(linkBinaryTree, "D", "E");
    linkBinaryTreeSetRightChild(linkBinaryTree, "D", "F");
    linkBinaryTreeSetRightChild(linkBinaryTree, "E", "G");
    return linkBinaryTree;
}

void testPointer() {
    // LinkBinaryTreeNode结构体 24字节
    printf("LinkBinaryTreeNode sizeof is %lu \n", sizeof(LinkBinaryTreeNode));

    // LinkBinaryTreeNode指针 8字节
    printf("LinkBinaryTreeNode sizeof is %lu \n", sizeof(LinkBinaryTreeNode));

    LinkBinaryTree *binaryTree = createBinaryTree();

    // 结构体起始地址
    printf("binaryTree p is %p \n", binaryTree);

    // 结构体起始地址
    printf("binaryTree->data p is %p \n", &(binaryTree->root->dataPointer));

    // 结构体起始地址 + 8
    printf("binaryTree->lchild p is %p \n", &(binaryTree->root->lchild));

    // 结构体起始地址 + 8 + 8
    printf("binaryTree->rchild p is %p \n", &(binaryTree->root->rchild));

    // binaryTree变量在栈中的地址
    printf("&binaryTree p is %p \n", &binaryTree);
}

void preorderTest() {
    printf("==========前序遍历执行效果如下==========\n");
    LinkBinaryTree *binaryTree = createBinaryTree();

    SinglyLinkedList *traversalResult = linkBinaryTreeTraversalGetData(binaryTree, preorder);
    printf("交换前↓↓↓\n");
    singlyLinkedListPrintf(traversalResult);

    printf("交换左右子树中...\n");
    linkBinaryTreeTraversalSwapChild(binaryTree, preorder);

    traversalResult = linkBinaryTreeTraversalGetData(binaryTree, preorder);
    printf("交换后↓↓↓\n");
    singlyLinkedListPrintf(traversalResult);
}

void inorderTest() {
    printf("==========中序遍历执行效果如下==========\n");
    LinkBinaryTree *binaryTree = createBinaryTree();

    SinglyLinkedList *traversalResult = linkBinaryTreeTraversalGetData(binaryTree, inorder);
    printf("交换前↓↓↓\n");
    singlyLinkedListPrintf(traversalResult);

    printf("交换左右子树中...\n");
    linkBinaryTreeTraversalSwapChild(binaryTree, inorder);

    traversalResult = linkBinaryTreeTraversalGetData(binaryTree, inorder);
    printf("交换后↓↓↓\n");
    singlyLinkedListPrintf(traversalResult);
}

void postorderTest() {
    printf("==========后序遍历执行效果如下==========\n");
    LinkBinaryTree *binaryTree = createBinaryTree();

    SinglyLinkedList *traversalResult = linkBinaryTreeTraversalGetData(binaryTree, postorder);
    printf("交换前↓↓↓\n");
    singlyLinkedListPrintf(traversalResult);

    printf("交换左右子树中...\n");
    linkBinaryTreeTraversalSwapChild(binaryTree, postorder);

    traversalResult = linkBinaryTreeTraversalGetData(binaryTree, postorder);
    printf("交换后↓↓↓\n");
    singlyLinkedListPrintf(traversalResult);
}

int main() {
    preorderTest();
    inorderTest();
    postorderTest();
}