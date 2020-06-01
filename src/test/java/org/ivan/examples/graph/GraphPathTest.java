package org.ivan.examples.graph;

import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GraphPathTest {
    @Test
    void nullVertexPath() {
        Graph<Vertex> g = Graph.newDirectedGraph();

        assertThrows(NullPointerException.class, () -> g.getPath(null, new Vertex()));
        assertThrows(NullPointerException.class, () -> g.getPath(new Vertex(), null));
    }

    @Test
    void alienVertexPath() {
        Graph<Vertex> g = Graph.newDirectedGraph();
        Vertex v = new Vertex();
        g.addVertex(v);

        assertThrows(IllegalArgumentException.class, () -> g.getPath(v, new Vertex()));
        assertThrows(IllegalArgumentException.class, () -> g.getPath(new Vertex(), v));
    }

    @Test
    void disjointVerticesPath() {
        Graph<Vertex> g = Graph.newDirectedGraph();
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();
        g.addVertex(v1);
        g.addVertex(v2);

        Graph.Path<Vertex> path = g.getPath(v1, v2);

        assertFalse(path.exists());
        assertThrows(IllegalStateException.class, path::edges);
    }

    @Test
    void identityPath() {
        Graph<Vertex> g = Graph.newDirectedGraph();
        Vertex v = new Vertex();
        g.addVertex(v);

        Graph.Path<Vertex> path = g.getPath(v, v);

        assertTrue(path.exists());
        assertTrue(path.edges().isEmpty());
    }

    @Test
    void oneEdgeDirectedPath() {
        Graph<Vertex> g = Graph.newDirectedGraph();
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addEdge(v1, v2);

        Graph.Path<Vertex> path = g.getPath(v1, v2);
        assertTrue(path.exists());
        assertEquals(1, path.edges().size());
        Graph.Edge<?> edge = path.edges().get(0);
        assertEquals(v1, edge.from());
        assertEquals(v2, edge.to());

        assertFalse(g.getPath(v2, v1).exists());
    }

    @Test
    void oneEdgeUndirectedPath() {
        Graph<Vertex> g = Graph.newUndirectedGraph();
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addEdge(v1, v2);

        {
            Graph.Path<Vertex> path = g.getPath(v1, v2);
            assertTrue(path.exists());
            assertEquals(1, path.edges().size());
            Graph.Edge<?> edge = path.edges().get(0);
            assertEquals(v1, edge.from());
            assertEquals(v2, edge.to());
        }

        {
            Graph.Path<Vertex> path = g.getPath(v2, v1);
            assertTrue(path.exists());
            assertEquals(1, path.edges().size());
            Graph.Edge<?> edge = path.edges().get(0);
            assertEquals(v2, edge.from());
            assertEquals(v1, edge.to());
        }
    }

    @Test
    void twoEdgeDirectedPath() {
        Graph<Vertex> g = Graph.newDirectedGraph();
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();
        Vertex v3 = new Vertex();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);

        Graph.Path<Vertex> path = g.getPath(v1, v3);
        assertTrue(path.exists());
        List<Graph.Edge<Vertex>> edges = path.edges();
        assertEquals(2, edges.size());
        Graph.Edge<?> e0 = edges.get(0);
        Graph.Edge<?> e1 = edges.get(1);
        assertEquals(v1, e0.from());
        assertEquals(v2, e0.to());
        assertEquals(v2, e1.from());
        assertEquals(v3, e1.to());

        assertFalse(g.getPath(v3, v1).exists());
    }

    @Test
    void twoEdgeUndirectedPath() {
        Graph<Vertex> g = Graph.newUndirectedGraph();
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();
        Vertex v3 = new Vertex();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addEdge(v1, v2);
        g.addEdge(v3, v2);

        {
            Graph.Path<Vertex> path = g.getPath(v1, v3);
            assertTrue(path.exists());
            List<Graph.Edge<Vertex>> edges = path.edges();
            assertEquals(2, edges.size());
            Graph.Edge<?> e0 = edges.get(0);
            Graph.Edge<?> e1 = edges.get(1);
            assertEquals(v1, e0.from());
            assertEquals(v2, e0.to());
            assertEquals(v2, e1.from());
            assertEquals(v3, e1.to());
        }

        {
            Graph.Path<Vertex> path = g.getPath(v3, v1);
            assertTrue(path.exists());
            List<Graph.Edge<Vertex>> edges = path.edges();
            assertEquals(2, edges.size());
            Graph.Edge<?> e0 = edges.get(0);
            Graph.Edge<?> e1 = edges.get(1);
            assertEquals(v3, e0.from());
            assertEquals(v2, e0.to());
            assertEquals(v2, e1.from());
            assertEquals(v1, e1.to());
        }
    }

    @Test
    void noPathAndCycle() {
        Graph<Vertex> g = Graph.newDirectedGraph();

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

        assertFalse(g.getPath(v1, v3).exists());
    }
}
