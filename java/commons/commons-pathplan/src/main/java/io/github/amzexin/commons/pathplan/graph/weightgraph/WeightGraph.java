package io.github.amzexin.commons.pathplan.graph.weightgraph;

import io.github.amzexin.commons.pathplan.graph.Graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

/**
 * Description:
 *
 * @author Lizexin
 * @date 2020-01-17 19:19
 */
public class WeightGraph<N, W extends Comparable<W>> implements Graph<N> {

    private int estimateVertexCount;

    private int edgeCount;

    private boolean direction;

    private Map<N, Map<N, WeightEdge<N, W>>> graph;

    private Set<N> vertexSet;


    public void addEdge(WeightEdge<N, W> edge) {
        N v = edge.getV();
        N w = edge.getW();

        if (hasEdge(v, w)) {
            return;
        }

        graph.get(v).put(w, edge);
        if (!direction) {
            graph.computeIfAbsent(w, new Function<N, Map<N, WeightEdge<N, W>>>() {
                @Override
                public Map<N, WeightEdge<N, W>> apply(N key) {
                    return new HashMap<>(estimateVertexCount);
                }
            }).put(v, new WeightEdge<>(w, v, edge.getWeight()));
        }

        vertexSet.add(v);
        vertexSet.add(w);
        edgeCount++;

    }

    @Override
    public void addEdge(N v, N w) {
        if (hasEdge(v, w)) {
            return;
        }

        graph.get(v).put(w, null);
        if (!direction) {
            graph.computeIfAbsent(w, new Function<N, Map<N, WeightEdge<N, W>>>() {
                @Override
                public Map<N, WeightEdge<N, W>> apply(N key) {
                    return new HashMap<>(estimateVertexCount);
                }
            }).put(v, new WeightEdge<>(w, v, null));
        }

        vertexSet.add(v);
        vertexSet.add(w);
        edgeCount++;
    }

    @Override
    public boolean hasEdge(N v, N w) {
        return graph.computeIfAbsent(v, new Function<N, Map<N, WeightEdge<N, W>>>() {
            @Override
            public Map<N, WeightEdge<N, W>> apply(N key) {
                return new HashMap<>(estimateVertexCount);
            }
        }).containsKey(w);

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
        return graph.get(v).keySet();
    }

    @Override
    public Iterable<N> allVertex() {
        return vertexSet;
    }

    public Iterable<WeightEdge<N, W>> adjacencyEdge(N v) {
        return graph.get(v).values();
    }

    public WeightGraph(int estimateVertexCount, boolean direction) {
        this.estimateVertexCount = estimateVertexCount;
        this.edgeCount = 0;
        this.direction = direction;
        this.vertexSet = new HashSet<>(estimateVertexCount);
        this.graph = new HashMap<>(estimateVertexCount);
    }

}
