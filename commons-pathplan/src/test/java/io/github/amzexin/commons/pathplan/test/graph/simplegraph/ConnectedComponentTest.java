package io.github.amzexin.commons.pathplan.test.graph.simplegraph;

import io.github.amzexin.commons.pathplan.graph.Graph;
import io.github.amzexin.commons.pathplan.graph.simplegraph.ConnectedComponent;
import io.github.amzexin.commons.pathplan.graph.simplegraph.SimpleGraph;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.MDC;

/**
 * Description:
 *
 * @author Vincent
 * @date 2019-12-31 00:26
 */
@Slf4j
public class ConnectedComponentTest {

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

        ConnectedComponent<Integer> connectedComponent = new ConnectedComponent<>(graph);
        MDC.put("trace_id", "lizxtest");
        log.info("cc count = {}", connectedComponent.connectedCount());

        log.info("0 = {}", connectedComponent.vertexConnectedId(0));
    }
}
