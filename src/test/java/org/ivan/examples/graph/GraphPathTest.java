package org.ivan.examples.graph;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphPathTest {
    @Test
    void nullVertexPath() {
        Graph g = new Graph();

        assertThrows(NullPointerException.class, () -> g.getPath(null, new Vertex()));
        assertThrows(NullPointerException.class, () -> g.getPath(new Vertex(), null));
    }

    @Test
    void alienVertexPath() {
        Graph g = new Graph();
        Vertex v = new Vertex();
        g.addVertex(v);

        assertThrows(IllegalArgumentException.class, () -> g.getPath(v, new Vertex()));
        assertThrows(IllegalArgumentException.class, () -> g.getPath(new Vertex(), v));
    }

    @Test
    void disjointVerticesPath() {
        Graph g = new Graph();
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();
        g.addVertex(v1);
        g.addVertex(v2);

        Optional<List<Graph.Edge>> opath = g.getPath(v1, v2);

        assertFalse(opath.isPresent());
    }

    @Test
    void identityPath() {
        Graph g = new Graph();
        Vertex v = new Vertex();
        g.addVertex(v);

        Optional<List<Graph.Edge>> opath = g.getPath(v, v);

        assertTrue(opath.isPresent());
        assertTrue(opath.get().isEmpty());
    }

    @Test
    void oneEdgePath() {
        Graph g = new Graph();
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addEdge(v1, v2);

        Optional<List<Graph.Edge>> opath1 = g.getPath(v1, v2);
        assertTrue(opath1.isPresent());
        assertEquals(1, opath1.get().size());
        Graph.Edge edge = opath1.get().get(0);
        assertEquals(v1, edge.from());
        assertEquals(v2, edge.to());

        assertFalse(g.getPath(v2, v1).isPresent());
    }

    @Test
    void twoEdgePath() {
        Graph g = new Graph();
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();
        Vertex v3 = new Vertex();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);

        Optional<List<Graph.Edge>> opath = g.getPath(v1, v3);
        assertTrue(opath.isPresent());
        List<Graph.Edge> path = opath.get();
        assertEquals(2, path.size());
        Graph.Edge e0 = path.get(0);
        Graph.Edge e1 = path.get(1);
        assertEquals(v1, e0.from());
        assertEquals(v2, e0.to());
        assertEquals(v2, e1.from());
        assertEquals(v3, e1.to());
    }

    @Test
    void noPathAndCycle() {
        Graph g = new Graph();

        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();
        Vertex v3 = new Vertex();
        Vertex v4 = new Vertex();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);
        g.addEdge(v1, v2);
        g.addEdge(v2, v1);
        g.addEdge(v3, v4);
        g.addEdge(v4, v3);

        assertFalse(g.getPath(v1, v3).isPresent());
    }
}
