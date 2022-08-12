package io.github.amzexin.commons.pathplan.graph.weightgraph;

import io.github.amzexin.commons.pathplan.common.IndexMinHeap;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Description: dijkstra
 *
 * @author Lizexin
 * @date 2019-12-31 13:53
 */
@Slf4j
public class DijkstraShortestPath<N> {

    private WeightGraph<N, Double> graph;

    private IndexMinHeap<N, WeightEdge<N, Double>> edgeMinHeap;

    private Set<WeightEdge<N, Double>> visitedEdge;

    /**
     * 最短路径中某顶点的上个顶点
     */
    private Map<N, WeightEdge<N, Double>> pre;

    /**
     * 源点到某顶点的最小权重
     */
    private Map<N, Double> weightTo;

    /**
     * 源点
     */
    private N sourceVertex;

    public List<N> shortestPath(N targetVertex) {
        if (!weightTo.containsKey(targetVertex)) {
            return Collections.emptyList();
        }

        LinkedList<N> path = new LinkedList<>();

        for (N currentVertex = targetVertex; currentVertex != sourceVertex; currentVertex = pre.get(currentVertex).other(currentVertex)) {
            path.addFirst(currentVertex);
        }

        path.addFirst(sourceVertex);

        return path;
    }

    public Double shortestWeight(N targetVertex) {
        return weightTo.get(targetVertex);
    }

    public DijkstraShortestPath(WeightGraph<N, Double> graph, N sourceVertex) {
        this.graph = graph;
        this.edgeMinHeap = new IndexMinHeap<>(graph.edgeCount());
        this.visitedEdge = new HashSet<>(graph.edgeCount());
        this.pre = new HashMap<>(graph.vertexCount());
        this.weightTo = new HashMap<>(graph.vertexCount());
        this.sourceVertex = sourceVertex;
        init();
    }

    private void init() {
        weightTo.put(sourceVertex, 0D);
        edgeMinHeap.put(sourceVertex, new WeightEdge<>(sourceVertex, sourceVertex, 0D));

        while (!edgeMinHeap.isEmpty()) {
            // 取出权重最小的一条边，进行计算
            N v = edgeMinHeap.popKey();

            Iterable<WeightEdge<N, Double>> adjacencyEdges = graph.adjacencyEdge(v);
            for (WeightEdge<N, Double> adjacencyEdge : adjacencyEdges) {
                // 访问过的边直接pass
                if (visitedEdge.contains(adjacencyEdge)) {
                    continue;
                }
                visitedEdge.add(adjacencyEdge);

                // 如果之前的边权重更小，不需要做任何修改
                N w = adjacencyEdge.other(v);
                if (weightTo.containsKey(w) && weightTo.get(v) + adjacencyEdge.getWeight() >= weightTo.get(w)) {
                    continue;
                }

                weightTo.put(w, weightTo.get(v) + adjacencyEdge.getWeight());
                edgeMinHeap.put(w, adjacencyEdge);
                pre.put(w, adjacencyEdge);
            }
        }
    }
}
