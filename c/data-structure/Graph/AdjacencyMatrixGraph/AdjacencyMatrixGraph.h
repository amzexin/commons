//
// Created by Vincent on 2021/5/9.
//

#ifndef INC_ADJACENCYMATRIXGRAPH_H
#define INC_ADJACENCYMATRIXGRAPH_H

#include <assert.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "SinglyLinkedList.h"
#include "ArrayList.h"

/**
 * 边结构体
 */
typedef struct {
    /**
     * from顶点
     */
    void *from;
    /**
     * to顶点
     */
    void *to;
    /**
     * 边的权重
     */
    double weight;
} AdjacencyMatrixEdge;

/**
 * 图结构体
 */
typedef struct {
    /**
     * 当前Graph可以容纳的最大顶点数量
     */
    int vertexCapacity;
    /**
     * 当前Graph已经容纳的顶点数量
     */
    int vertexCount;
    /**
     * 顶点数据大小
     */
    int vertexDataSize;
    /**
     * 所有的顶点
     */
    ArrayList *vertexList;
    /**
     * 邻接矩阵；二维表示；二维压缩成一维用
     */
    double *matrix;
    /**
     * 是否有向
     */
    bool direction;

    /**
     * 比较顶点的函数指针
     */
    bool (*equalsVertex)(void *, void *);

    /**
     * toString元素的函数指针
     */
    char *(*toStringElement)(void *);
} AdjacencyMatrixGraph;

/**
 * 遍历类型
 */
typedef enum {
    /**
     * 深度优先遍历
     */
    DFS = 0,
    /**
     * 广度优先遍历
     */
    BFS = 1
} AdjacencyMatrixGraphTraversalType;


/**
 * 打印Graph
 *
 * @param graph
 */
void adjacencyMatrixGraphPrintf(AdjacencyMatrixGraph *graph);

/**
 * 初始化一个图
 *
 * @param initCapacity 初始容量
 * @param direction 是否有向
 * @param equalsVertex 比较顶点，相通时返回true，否则返回false
 * @return 图指针
 */
AdjacencyMatrixGraph *adjacencyMatrixGraphInitiate(int initCapacity, bool direction,
                                                   bool (equalsVertex)(void *, void *),
                                                   char *(*toStringElement)(void *));

/**
 * 插入边
 *
 * @param graph 图指针
 * @param edge 边
 */
void adjacencyMatrixGraphInsertEdge(AdjacencyMatrixGraph *graph, AdjacencyMatrixEdge edge);

/**
 * 删除边
 *
 * @param graph 图指针
 * @param from from顶点
 * @param to to顶点
 */
void adjacencyMatrixGraphDeleteEdge(AdjacencyMatrixGraph *graph, void *from, void *to);

/**
 * 深度优先遍历
 *
 * @param graph
 * @param traversalResult
 */
void adjacencyMatrixGraphDFS(AdjacencyMatrixGraph *graph, SinglyLinkedList *traversalResult);

/**
 * 广度优先遍历
 *
 * @param graph
 * @param traversalResult
 */
void adjacencyMatrixGraphBFS(AdjacencyMatrixGraph *graph, SinglyLinkedList *traversalResult);

/**
 * 检查是否为连通图
 *
 * @param graph
 * @return
 */
bool adjacencyMatrixGraphIsConnected(AdjacencyMatrixGraph *graph);

/**
 * Prim算法获取最小生成树
 *
 * @param graph
 * @return 最小生成树 LinkList<Edge>
 */
SinglyLinkedList *adjacencyMatrixGraphPrim(AdjacencyMatrixGraph *graph);

/**
 * 拓扑排序并按层级打印
 *
 * @param graph
 */
void adjacencyMatrixGraphTopologicalSortingLevelPrintf(AdjacencyMatrixGraph *graph);

#endif  // INC_ADJACENCYMATRIXGRAPH_H
