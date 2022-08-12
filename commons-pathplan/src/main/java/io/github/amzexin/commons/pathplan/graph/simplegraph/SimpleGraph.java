package io.github.amzexin.commons.pathplan.graph.simplegraph;

import io.github.amzexin.commons.pathplan.graph.Graph;

import java.util.*;
import java.util.function.Function;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2020-01-18 14:30
 */
public class SimpleGraph<N> implements Graph<N> {

    private int estimateVertexCount;

    private int edgeCount;

    private boolean direction;

    private Map<N, Set<N>> graph;

    private Set<N> vertexSet;

    @Override
    public void addEdge(N v, N w) {
        if (hasEdge(v, w)) {
            return;
        }

        graph.get(v).add(w);
        if (!direction) {
            graph.computeIfAbsent(w, new Function<N, Set<N>>() {
                @Override
                public Set<N> apply(N n) {
                    return new TreeSet<>();
                }
            }).add(v);
        }

        vertexSet.add(v);
        vertexSet.add(w);
        edgeCount++;
    }


    @Override
    public boolean hasEdge(N v, N w) {
        return graph.computeIfAbsent(v, new Function<N, Set<N>>() {
            @Override
            public Set<N> apply(N n) {
                return new TreeSet<>();
            }
        }).contains(w);
    }

    @Override
    public int vertexCount() {
        return vertexSet.size();
    }

    @Override
    public int edgeCount() {
        return edgeCount;
    }

    @Override
    public Iterable<N> adjacencyVertex(N v) {
        return graph.get(v);
    }

    @Override
    public Iterable<N> allVertex() {
        return vertexSet;
    }

    public SimpleGraph(int estimateVertexCount, boolean direction) {
        this.estimateVertexCount = estimateVertexCount;
        this.edgeCount = 0;
        this.direction = direction;
        this.vertexSet = new HashSet<>(estimateVertexCount);
        this.graph = new HashMap<>(estimateVertexCount);
    }

}
