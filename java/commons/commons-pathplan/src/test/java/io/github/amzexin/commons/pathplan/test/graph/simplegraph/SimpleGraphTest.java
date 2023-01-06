package io.github.amzexin.commons.pathplan.test.graph.simplegraph;

import io.github.amzexin.commons.pathplan.graph.Graph;
import io.github.amzexin.commons.pathplan.graph.simplegraph.SimpleGraph;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Iterator;

/**
 * Description:
 *
 * @author Vincent
 * @date 2019-12-30 23:47
 */
@Slf4j
public class SimpleGraphTest {

    @Test
    public void case1() {
        Graph<Integer> graph = new SimpleGraph<>(7, false);

        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(0, 5);
        graph.addEdge(0, 6);
        graph.addEdge(3, 4);
        graph.addEdge(3, 5);
        graph.addEdge(4, 5);
        graph.addEdge(4, 6);

        Iterator<Integer> vVertexIterator = graph.allVertex().iterator();
        while (vVertexIterator.hasNext()) {
            StringBuilder sb = new StringBuilder();

            Integer v = vVertexIterator.next();
            sb.append(v).append(" : ");

            Iterator<Integer> wVertexIterator = graph.adjacencyVertex(v).iterator();
            while (wVertexIterator.hasNext()) {
                sb.append(wVertexIterator.next()).append(" ");
            }

            log.info(sb.toString());
        }


    }
}
