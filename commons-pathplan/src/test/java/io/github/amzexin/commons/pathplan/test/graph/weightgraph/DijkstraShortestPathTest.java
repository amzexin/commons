package io.github.amzexin.commons.pathplan.test.graph.weightgraph;

import io.github.amzexin.commons.pathplan.graph.weightgraph.DijkstraShortestPath;
import io.github.amzexin.commons.pathplan.graph.weightgraph.WeightEdge;
import io.github.amzexin.commons.pathplan.graph.weightgraph.WeightGraph;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

/**
 * Description:
 *
 * @author Vincent
 * @date 2019-12-31 22:27
 */
@Slf4j
public class DijkstraShortestPathTest {

    @Test
    public void case1() {
        WeightGraph<Integer, Double> graph = new WeightGraph<>(7, false);

        graph.addEdge(new WeightEdge<>(0, 1, 8D));
        graph.addEdge(new WeightEdge<>(0, 2, 8D));
        graph.addEdge(new WeightEdge<>(0, 5, 15D));
        graph.addEdge(new WeightEdge<>(0, 6, 20D));
        graph.addEdge(new WeightEdge<>(1, 2, 8D));
        graph.addEdge(new WeightEdge<>(1, 3, 9D));
        graph.addEdge(new WeightEdge<>(1, 5, 11D));
        graph.addEdge(new WeightEdge<>(2, 3, 10D));
        graph.addEdge(new WeightEdge<>(2, 4, 19D));
        graph.addEdge(new WeightEdge<>(2, 6, 13D));
        graph.addEdge(new WeightEdge<>(3, 4, 12D));
        graph.addEdge(new WeightEdge<>(3, 5, 12D));
        graph.addEdge(new WeightEdge<>(4, 5, 20D));
        graph.addEdge(new WeightEdge<>(4, 6, 15D));

        DijkstraShortestPath<Integer> dijkstraShortestPath = new DijkstraShortestPath<>(graph, 0);

        List<Integer> path = dijkstraShortestPath.shortestPath(4);
        log.info("0 -> 4 path = {}", path.toString());
    }

    @Test
    public void case2() {
        WeightGraph<Integer, Double> graph = new WeightGraph<>(5, false);

        graph.addEdge(new WeightEdge<>(0, 1, 5D));
        graph.addEdge(new WeightEdge<>(0, 2, 2D));
        graph.addEdge(new WeightEdge<>(0, 3, 6D));
        graph.addEdge(new WeightEdge<>(1, 4, 1D));
        graph.addEdge(new WeightEdge<>(2, 1, 1D));
        graph.addEdge(new WeightEdge<>(2, 4, 5D));
        graph.addEdge(new WeightEdge<>(2, 3, 3D));
        graph.addEdge(new WeightEdge<>(3, 4, 2D));

        DijkstraShortestPath<Integer> dijkstraShortestPath = new DijkstraShortestPath<>(graph, 0);

        Iterable<Integer> allVertex = graph.allVertex();
        for (Integer targetVertex : allVertex) {
            if (targetVertex == 0)
                continue;

            Double weight = dijkstraShortestPath.shortestWeight(targetVertex);
            List<Integer> path = dijkstraShortestPath.shortestPath(targetVertex);
            log.info("0 -> {} : weight = {}, path = {}", targetVertex, weight, path.toString());
        }
    }

    @Test
    public void case3() {
        WeightGraph<Integer, Double> graph = new WeightGraph<>(5, false);

        graph.addEdge(new WeightEdge<>(0, 1, 4D));
        graph.addEdge(new WeightEdge<>(0, 2, 2D));
        graph.addEdge(new WeightEdge<>(1, 2, 1D));
        graph.addEdge(new WeightEdge<>(1, 3, 2D));
        graph.addEdge(new WeightEdge<>(1, 4, 3D));
        graph.addEdge(new WeightEdge<>(2, 3, 4D));
        graph.addEdge(new WeightEdge<>(2, 4, 5D));
        graph.addEdge(new WeightEdge<>(3, 4, 1D));

        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(graph, 0);

        Iterable<Integer> allVertex = graph.allVertex();
        for (Integer targetVertex : allVertex) {
            if (targetVertex == 0)
                continue;

            Double weight = dijkstraShortestPath.shortestWeight(targetVertex);
            List<Integer> path = dijkstraShortestPath.shortestPath(targetVertex);
            log.info("0 -> {} : weight = {}, path = {}", targetVertex, weight, path.toString());
        }
    }

    @Test
    public void maxBug() {
        WeightEdge<Integer, Double> edge = new WeightEdge<>(2, 4, 19.0D);
        System.out.println(edge.other(4));
    }
}
