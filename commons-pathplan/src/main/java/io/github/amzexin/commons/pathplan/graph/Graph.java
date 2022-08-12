package io.github.amzexin.commons.pathplan.graph;

/**
 * Description:
 *
 * @author Vincent
 * @date 2020-01-18 15:17
 */
public interface Graph<N> {

    /**
     * add edge
     *
     * @param v
     * @param w
     */
    void addEdge(N v, N w);

    /**
     * has edge
     *
     * @param v
     * @param w
     * @return
     */
    boolean hasEdge(N v, N w);

    /**
     * vertex count
     *
     * @return
     */
    int vertexCount();

    /**
     * edge count
     *
     * @return
     */
    int edgeCount();

    /**
     * adjacency vertex
     *
     * @param v
     * @return
     */
    Iterable<N> adjacencyVertex(N v);

    /**
     * all vertex
     *
     * @return
     */
    Iterable<N> allVertex();

}
