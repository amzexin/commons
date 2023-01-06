//
// Created by Vincent on 2021/5/9.
//

#include "AdjacencyMatrixGraph.h"

#include "SinglyLinkedQueue.h"

/**
 * 二维坐标转一维坐标
 *
 * @param x 矩阵横坐标
 * @param y 矩阵纵坐标
 * @param length 矩阵的宽度
 * @return
 */
int indexTransform(int x, int y, int length) { return x * length + y; }

void adjacencyMatrixGraphPrintf(AdjacencyMatrixGraph *graph) {
    int vCount = graph->vertexCount;
    ArrayList *vertexList = graph->vertexList;

    // 打印第一行
    printf("\t\t");
    for (int i = 0; i < vCount; ++i) {
        printf("%s\t\t", vertexList->toStringElement(arrayListGetElement(vertexList, i)));
    }
    printf("\n");

    double *matrix = graph->matrix;
    int vCapacity = graph->vertexCapacity;
    for (int i = 0; i < vCount; ++i) {
        printf("%s\t\t", vertexList->toStringElement(arrayListGetElement(vertexList, i)));
        for (int j = 0; j < vCount; ++j) {
            printf("%-4.1f\t", matrix[indexTransform(i, j, vCapacity)]);
        }
        printf("\n");
    }
}

AdjacencyMatrixGraph *adjacencyMatrixGraphInitiate(int initCapacity, bool direction,
                                                   bool (*equalsVertex)(void *, void *),
                                                   char *(*toStringElement)(void *)) {
    if (initCapacity < 2) {
        printf("initCapacity不允许小于2");
        exit(1);
    }
    AdjacencyMatrixGraph *graph = malloc(sizeof(AdjacencyMatrixGraph));
    graph->vertexCapacity = initCapacity;
    graph->vertexCount = 0;
    graph->vertexList = arrayListInitiate(initCapacity * initCapacity, equalsVertex, toStringElement);
    graph->direction = direction;
    graph->equalsVertex = equalsVertex;

    // 创建邻接矩阵，并全部初始化为0
    double *matrix = malloc(sizeof(double) * initCapacity * initCapacity);
    graph->matrix = matrix;
    for (int i = 0; i < initCapacity; ++i) {
        for (int j = 0; j < initCapacity; ++j) {
            matrix[indexTransform(i, j, initCapacity)] = 0;
        }
    }
    return graph;
}

void loopEdgeCheck(AdjacencyMatrixEdge edge) {
    void *from = edge.from;
    void *to = edge.to;

    if (strcmp(from, to) == 0) {
//        printf("不允许自环边(%s, %s)", from, to);
        printf("不允许自环边");
        exit(1);
    }
}

void expansion(AdjacencyMatrixGraph *graph) {
    // 扩容 vertexCapacity
    int old_vCapacity = graph->vertexCapacity;
    int new_vCapacity = old_vCapacity * 2;
    graph->vertexCapacity = new_vCapacity;

    // 扩容 vertexList
//    ArrayList *oldVertexList = graph->vertexList;
//    ArrayList *newVertexList = arrayListInitiate(new_vCapacity, graph->equalsVertex, graph->toStringElement);;
//    graph->vertexList = newVertexList;
//    for (int i = 0; i < old_vCapacity; ++i) {
//        void *oldVertex = arrayListGetElement(oldVertexList, i);
//        arrayListInsertElement(newVertexList, oldVertex, i);
//    }

    // 扩容 matrix
    double *old_matrix = graph->matrix;
    double *new_matrix = malloc(sizeof(double) * new_vCapacity * new_vCapacity);
    graph->matrix = new_matrix;
    for (int i = 0; i < new_vCapacity; ++i) {
        for (int j = 0; j < new_vCapacity; ++j) {
            if (i < old_vCapacity && j < old_vCapacity) {
                // 拷贝原来的数据
                new_matrix[indexTransform(i, j, new_vCapacity)] =
                        old_matrix[indexTransform(i, j, old_vCapacity)];
            } else {
                // 为新扩容的空间设置初始值
                new_matrix[indexTransform(i, j, new_vCapacity)] = 0;
            }
        }
    }

}

void getIndex(AdjacencyMatrixGraph *graph, void *from, void *to, int *fromIndex, int *toIndex) {
    int vCount = graph->vertexCount;
    ArrayList *vertexList = graph->vertexList;
    bool (*equalsVertex)(void *, void *) = graph->equalsVertex;

    // 寻找from和to所在下标
    for (int i = 0; i < vCount && (*fromIndex == -1 || *toIndex == -1); ++i) {
        void *currentVertex = arrayListGetElement(vertexList, i);
        if (equalsVertex(currentVertex, from) == true) {
            *fromIndex = i;
        }
        if (equalsVertex(currentVertex, to) == true) {
            *toIndex = i;
        }
    }

    // 找到了，直接返回
    if (*fromIndex != -1 && *toIndex != -1) {
        return;
    }

    // 目前Graph无法容纳新的顶点，需要扩容
    int vCapacity = graph->vertexCapacity;
    if (vCount + 2 > vCapacity) {
        expansion(graph);
    }

    vertexList = graph->vertexList;

    // 添加from
    if (*fromIndex == -1) {
        *fromIndex = graph->vertexCount++;
        arrayListInsertElement(vertexList, from, *fromIndex);
    }

    // 添加to
    if (*toIndex == -1) {
        *toIndex = graph->vertexCount++;
        arrayListInsertElement(vertexList, to, *toIndex);
    }
}

void adjacencyMatrixGraphInsertEdge(AdjacencyMatrixGraph *graph, AdjacencyMatrixEdge edge) {
    loopEdgeCheck(edge);

    int fromIndex = -1;
    int toIndex = -1;
    getIndex(graph, edge.from, edge.to, &fromIndex, &toIndex);

    assert(fromIndex != -1);
    assert(toIndex != -1);

    double *matrix = graph->matrix;
    int newIndex = indexTransform(fromIndex, toIndex, graph->vertexCapacity);
    matrix[newIndex] = edge.weight;

    if (!graph->direction) {
        newIndex = indexTransform(toIndex, fromIndex, graph->vertexCapacity);
        matrix[newIndex] = edge.weight;
    }
}

/**
 * 删除边
 *
 * @param graph 图指针
 * @param from from顶点
 * @param to to顶点
 */
void adjacencyMatrixGraphDeleteEdge(AdjacencyMatrixGraph *graph, void *from, void *to) {
    int fromIndex = -1;
    int toIndex = -1;
    getIndex(graph, from, to, &fromIndex, &toIndex);

    assert(fromIndex != -1);
    assert(toIndex != -1);

    double *matrix = graph->matrix;
    int newIndex = indexTransform(fromIndex, toIndex, graph->vertexCapacity);
    matrix[newIndex] = 0;

    if (!graph->direction) {
        newIndex = indexTransform(toIndex, fromIndex, graph->vertexCapacity);
        matrix[newIndex] = 0;
    }
}

void internalDFS(AdjacencyMatrixGraph *graph, int visited[], int curVerIndex,
                 SinglyLinkedList *traversalResult) {
    if (visited[curVerIndex] == 1) {
        return;
    }

    // 将当前元素标记为已访问
    ArrayList *vertexList = graph->vertexList;
    void *currentVertex = arrayListGetElement(vertexList, curVerIndex);
    singlyLinkedListAddElement(traversalResult, currentVertex);
    visited[curVerIndex] = 1;

    // 获取与当前顶点相邻接的顶点
    double *matrix = graph->matrix;
    int vCount = graph->vertexCount;
    int vCapacity = graph->vertexCapacity;
    for (int j = 0; j < vCount; ++j) {
        // 访问过得，直接跳过
        if (visited[curVerIndex] == 1) {
            continue;
        }
        // 与当前顶点不相邻接的顶点，直接跳过
        if (!matrix[indexTransform(curVerIndex, j, vCapacity)]) {
            continue;
        }
        // 与当前顶点相连接，执行dfs
        internalDFS(graph, visited, j, traversalResult);
    }
}

void adjacencyMatrixGraphDFS(AdjacencyMatrixGraph *graph, SinglyLinkedList *traversalResult) {
    int vCount = graph->vertexCount;

    // 创建并初始化visited
    int visited[vCount];
    for (int i = 0; i < vCount; ++i) {
        visited[i] = 0;
    }

    // 执行真正的dfs
    for (int i = 0; i < vCount; ++i) {
        internalDFS(graph, visited, i, traversalResult);
    }
}

void adjacencyMatrixGraphBFS(AdjacencyMatrixGraph *graph, SinglyLinkedList *traversalResult) {
    int vCount = graph->vertexCount;
    ArrayList *vertexList = graph->vertexList;
    double *matrix = graph->matrix;
    int vCapacity = graph->vertexCapacity;
    bool (*equalsVertex)(void *, void *) = graph->equalsVertex;
    char *(*toStringElement)(void *) = graph->toStringElement;

    // 创建并初始化visited
    int visited[vCount];
    for (int i = 0; i < vCount; ++i) {
        visited[i] = 0;
    }

    // 创建一个队列
    SinglyLinkedQueue *linkQueue = singlyLinkedQueueInitiate(graph->equalsVertex, graph->toStringElement);

    // 将第一个元素入队(打印，入队，标记为已访问)
    void *firstVertex = arrayListGetElement(vertexList, 0);
    singlyLinkedListAddElement(traversalResult, firstVertex);

    int *firstContent = malloc(sizeof(int));
    *firstContent = 0;
    singlyLinkedQueueEnElement(linkQueue, firstContent);
    visited[0] = 1;

    // 开始真正的BFS
    while (!singlyLinkedQueueIsEmpty(linkQueue)) {
        int *firstData = singlyLinkedQueueOutElement(linkQueue);
        int curIndex = *firstData;

        // 获取与firstData相邻且没有访问过的结点，然后添加到队列中并标记为已访问
        for (int j = 0; j < vCount; ++j) {
            // 访问过得，直接跳过
            if (visited[j]) {
                continue;
            }
            // 不相邻的顶点，直接跳过
            if (!matrix[indexTransform(curIndex, j, vCapacity)]) {
                continue;
            }

            // 入队操作
            void *currentVertex = arrayListGetElement(vertexList, j);
            singlyLinkedListAddElement(traversalResult, currentVertex);
            int *content = malloc(sizeof(int));
            *content = j;
            singlyLinkedQueueEnElement(linkQueue, content);
            visited[j] = 1;
        }
    }
}

bool adjacencyMatrixGraphIsConnected(AdjacencyMatrixGraph *graph) {
    SinglyLinkedList *bfsTraversalResult = singlyLinkedListInitiate(graph->equalsVertex, graph->toStringElement);
    adjacencyMatrixGraphDFS(graph, bfsTraversalResult);

    return graph->vertexCount == bfsTraversalResult->length;
}

SinglyLinkedList *adjacencyMatrixGraphPrim(AdjacencyMatrixGraph *graph) {
    /**
     * 先检查当前graph是否为连通图
     * 当前Graph所有点集
     * 当前生成树所有点集
     * 当LinkList.length == Graph.vertexCount - 1时，结束
     */
    bool connected = adjacencyMatrixGraphIsConnected(graph);
    if (!connected) {
        printf("当前Graph不是一个连通图");
        exit(1);
    }

    SinglyLinkedList *result = singlyLinkedListInitiate(graph->equalsVertex, graph->toStringElement);

    return result;
}

void adjacencyMatrixGraphTopologicalSortingLevelPrintf(AdjacencyMatrixGraph *graph) {
    // 原邻接矩阵
    double *matrix = graph->matrix;
    // 顶点数量
    int vertexCount = graph->vertexCount;
    // 顶点容量
    int vertexCapacity = graph->vertexCapacity;
    // 所有顶点
    ArrayList *vertexList = graph->vertexList;
    // 拷贝原矩阵，用于拓扑排序
    double *topologicalSorting_matrix = malloc(sizeof(double) * vertexCount * vertexCount);
    for (int i = 0; i < vertexCount; ++i) {
        for (int j = 0; j < vertexCount; ++j) {
            topologicalSorting_matrix[indexTransform(i, j, vertexCount)] =
                    matrix[indexTransform(i, j, vertexCapacity)];
        }
    }
    // 初始化所有结点的等级为0
    int level[vertexCount];
    for (int i = 0; i < vertexCount; ++i) {
        level[i] = 0;
    }
    // 开始拓扑排序
    int curWhileCount = 1;
    // 拓扑排序次数不应该大于顶点个数
    while (curWhileCount <= vertexCount) {
        // 挨个检查顶点是否可以打印
        for (int i = 0; i < vertexCount; ++i) {
            // 等级不为0，说明访问过了
            if (level[i] != 0) {
                continue;
            }
            // 检查是否有父结点
            bool hasParent = false;
            for (int j = 0; j < vertexCount; ++j) {
                if (i == j) {
                    continue;
                }
                if (topologicalSorting_matrix[indexTransform(j, i, vertexCount)] > 0) {
                    hasParent = true;
                    break;
                }
            }
            // 有父结点，说明还轮不到当前结点打印
            if (hasParent) {
                continue;
            }
            // 没有父结点，设置当前结点层级
            level[i] = curWhileCount;
        }

        // 打印当前等级的所有结点，并且将所有出度去掉
        for (int i = 0; i < vertexCount; ++i) {
            // 与当前等级不一致，不需要打印
            if (level[i] != curWhileCount) {
                continue;
            }
            // 打印该结点
            printf("%s\t", vertexList->toStringElement(arrayListGetElement(vertexList, i)));
            // 将所有出度去掉
            for (int j = 0; j < vertexCount; ++j) {
                if (i == j) {
                    continue;
                }
                topologicalSorting_matrix[indexTransform(i, j, vertexCount)] = 0;
            }

        }
        printf("\n");
        curWhileCount++;
    }
}