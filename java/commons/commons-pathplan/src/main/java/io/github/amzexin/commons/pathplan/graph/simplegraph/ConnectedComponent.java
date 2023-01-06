package io.github.amzexin.commons.pathplan.graph.simplegraph;


import io.github.amzexin.commons.pathplan.graph.Graph;

import java.util.*;

/**
 * Description: connected component
 *
 * @author Vincent
 * @date 2019-12-31 00:16
 */
public class ConnectedComponent<N> {

    private Graph<N> graph;

    private int count;

    private Set<N> visited;

    private Map<N, Integer> countMap;

    /**
     * 联通分量个数
     *
     * @return
     */
    public int connectedCount() {
        return count;
    }

    /**
     * 某个顶点所在的联通分量
     *
     * @param vertex
     * @return
     */
    public int vertexConnectedId(N vertex) {
        return countMap.get(vertex);
    }

    public ConnectedComponent(Graph<N> graph) {
        this.graph = graph;
        this.visited = new HashSet<>(graph.vertexCount());
        this.countMap = new HashMap<>(graph.vertexCount());
        init();
    }

    private void init() {
        Iterator<N> vertexIterator = graph.allVertex().iterator();
        while (vertexIterator.hasNext()) {
            N v = vertexIterator.next();
            if (!visited.contains(v)) {
                dfs(v);
                count++;
            }
        }
    }

    private void dfs(N v) {
        visited.add(v);
        countMap.put(v, count);
        Iterator<N> adjacencyVertex = graph.adjacencyVertex(v).iterator();
        while (adjacencyVertex.hasNext()) {
            N w = adjacencyVertex.next();
            if (!visited.contains(w)) {
                dfs(w);
            }
        }
    }
}
