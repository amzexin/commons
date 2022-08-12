package io.github.amzexin.commons.pathplan.graph.simplegraph;

import io.github.amzexin.commons.pathplan.graph.Graph;

import java.util.*;

/**
 * Description: shortest path
 *
 * @author Vincent
 * @date 2019-12-31 00:30
 */
public class ShortestPath<N> {

    private Graph<N> graph;

    private Set<N> visited;

    private N sourceVertex;

    private Map<N, N> pre;

    public List<N> path(N w) {
        if (!visited.contains(w)) {
            return Collections.emptyList();
        }

        LinkedList<N> path = new LinkedList<>();

        for (N current = w; !current.equals(sourceVertex); current = pre.get(current)) {
            path.addFirst(current);
        }
        path.addFirst(sourceVertex);

        return path;
    }

    public ShortestPath(Graph<N> graph, N sourceVertex) {
        this.graph = graph;
        this.visited = new HashSet<>(graph.vertexCount());
        this.sourceVertex = sourceVertex;
        this.pre = new HashMap<>(graph.vertexCount());

        init();
    }

    private void init() {
        bfs();
    }

    private void bfs() {
        Queue<N> queue = new LinkedList<>();
        queue.add(sourceVertex);
        visited.add(sourceVertex);
        pre.put(sourceVertex, sourceVertex);

        while (!queue.isEmpty()) {
            N v = queue.remove();
            Iterator<N> adjacencyVertex = graph.adjacencyVertex(v).iterator();
            while (adjacencyVertex.hasNext()) {
                N w = adjacencyVertex.next();
                if (!visited.contains(w)) {
                    queue.add(w);
                    visited.add(w);
                    pre.put(w, v);
                }
            }
        }
    }
}
