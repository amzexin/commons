//
// Created by Vincent on 2021/5/8.
//

#include "AdjacencyMatrixGraph.h"
#include "CommonUtil.h"

/**
 * 有向图
 */
AdjacencyMatrixGraph *directedGraph() {
    AdjacencyMatrixGraph *graph = adjacencyMatrixGraphInitiate(5, true, equalsString, toStringString);

    AdjacencyMatrixEdge edge;
    edge.from = "vo";
    edge.to = "v1";
    edge.weight = 30;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "vo";
    edge.to = "v2";
    edge.weight = 10;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "v1";
    edge.to = "v2";
    edge.weight = 50;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "v1";
    edge.to = "v3";
    edge.weight = 10;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "v3";
    edge.to = "v2";
    edge.weight = 60;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    adjacencyMatrixGraphPrintf(graph);

    return graph;
}

/**
 * 无向图
 */
AdjacencyMatrixGraph *undirectedGraph() {
    AdjacencyMatrixGraph *graph = adjacencyMatrixGraphInitiate(3, false, equalsString, toStringString);

    AdjacencyMatrixEdge edge;
    edge.from = "v0";
    edge.to = "v1";
    edge.weight = 50;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "v1";
    edge.to = "v2";
    edge.weight = 10;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "v2";
    edge.to = "v3";
    edge.weight = 20;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "v0";
    edge.to = "v4";
    edge.weight = 20;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "v4";
    edge.to = "v2";
    edge.weight = 30;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "v3";
    edge.to = "v0";
    edge.weight = 40;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    adjacencyMatrixGraphPrintf(graph);

    return graph;
}

void oldTest() {
    AdjacencyMatrixGraph *graph = directedGraph();

    SinglyLinkedList *dfsTraversalResult = singlyLinkedListInitiate(graph->equalsVertex, graph->toStringElement);
    SinglyLinkedList *bfsTraversalResult = singlyLinkedListInitiate(graph->equalsVertex, graph->toStringElement);

    adjacencyMatrixGraphDFS(graph, dfsTraversalResult);
    printf("dfs ==> ");
    singlyLinkedListPrintf(dfsTraversalResult);
    printf("\n");

    adjacencyMatrixGraphBFS(graph, bfsTraversalResult);
    printf("adjacencyMatrixGraphBFS ==> ");
    singlyLinkedListPrintf(bfsTraversalResult);
    printf("\n");

    bool connected = adjacencyMatrixGraphIsConnected(graph);
    printf("connected = %s \n", connected ? "true" : "false");
}

void topologicalSortingLevelPrintfTest() {
    AdjacencyMatrixGraph *graph = adjacencyMatrixGraphInitiate(5, true, equalsString, toStringString);

    AdjacencyMatrixEdge edge;
    edge.from = "A";
    edge.to = "C";
    edge.weight = 1;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "A";
    edge.to = "D";
    edge.weight = 1;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "B";
    edge.to = "D";
    edge.weight = 1;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "B";
    edge.to = "E";
    edge.weight = 1;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "C";
    edge.to = "F";
    edge.weight = 1;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "D";
    edge.to = "F";
    edge.weight = 1;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    edge.from = "E";
    edge.to = "F";
    edge.weight = 1;
    adjacencyMatrixGraphInsertEdge(graph, edge);

    adjacencyMatrixGraphTopologicalSortingLevelPrintf(graph);
}

int main() {
    topologicalSortingLevelPrintfTest();
    return 0;
}