package org.ivan.examples.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GraphBuildTest {
    @Test
    void addNullVertex() {
        Graph g = new Graph();

        assertThrows(NullPointerException.class, () -> g.addVertex(null));
    }

    @Test
    void addSameVertexTwice() {
        Graph g = new Graph();
        Vertex v = new Vertex();
        g.addVertex(v);

        assertThrows(IllegalArgumentException.class, () -> g.addVertex(v));
    }

    @Test
    void addNullVertexEdge() {
        Graph g = new Graph();
        assertThrows(NullPointerException.class, () -> g.addEdge(null, new Vertex()));
        assertThrows(NullPointerException.class, () -> g.addEdge(new Vertex(), null));
    }

    @Test
    void addNoVertexEdge() {
        Graph g = new Graph();

        assertThrows(IllegalArgumentException.class, () -> g.addEdge(new Vertex(), new Vertex()));
    }

    @Test
    void addNoSourceVertexEdge() {
        Graph g = new Graph();
        Vertex v = new Vertex();
        g.addVertex(v);

        assertThrows(IllegalArgumentException.class, () -> g.addEdge(new Vertex(), v));
    }

    @Test
    void addNoTargetVertexEdge() {
        Graph g = new Graph();
        Vertex v = new Vertex();
        g.addVertex(v);

        assertThrows(IllegalArgumentException.class, () -> g.addEdge(v, new Vertex()));
    }

    @Test
    void addIdentityEdge() {
        Graph g = new Graph();
        Vertex v = new Vertex();
        g.addVertex(v);

        assertThrows(IllegalArgumentException.class, () -> g.addEdge(v, v));
    }

    @Test
    void addSameEdgeTwice() {
        Graph g = new Graph();
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();
        g.addVertex(v1);
        g.addVertex(v2);
        g.addEdge(v1, v2);

        assertThrows(IllegalArgumentException.class, () -> g.addEdge(v1, v2));
    }

    @Test
    void addInverseEdges() {
        Graph g = new Graph();
        Vertex v1 = new Vertex();
        Vertex v2 = new Vertex();
        g.addVertex(v1);
        g.addVertex(v2);

        g.addEdge(v1, v2);
        g.addEdge(v2, v1);
        // assert no exception here
    }
}