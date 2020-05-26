package org.ivan.examples.graph;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GraphUserVertexTest {
    @Test
    void graphUsesOverriddenVertexEquals() {
        Graph<String> g = Graph.newDirectedGraph();
        g.addVertex("a");
        g.addVertex("b");
        g.addEdge("a", "b");

        List<Graph.Edge<String>> path = g.getPath("a", "b").edges();

        assertEquals(1, path.size());
        assertEquals("a", path.get(0).from());
        assertEquals("b", path.get(0).to());
    }
}
