package org.ivan.examples.graph;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Graph {
    private static class DirectedEdge {
        private final Vertex from;
        private final Vertex to;

        private DirectedEdge(Vertex from, Vertex to) {
            this.from = Objects.requireNonNull(from);
            this.to = Objects.requireNonNull(to);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            DirectedEdge edge = (DirectedEdge)o;
            return Objects.equals(from, edge.from) &&
                Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }

    private final Set<Vertex> vertices = new HashSet<>();
    private final Set<DirectedEdge> edges = new HashSet<>();

    public void addVertex(Vertex v) {
        Objects.requireNonNull(v);
        if (!vertices.add(v)) {
            throw new IllegalArgumentException("Vertex is already in graph: " + v);
        }
    }

    public void addEdge(Vertex v1, Vertex v2) {
        Objects.requireNonNull(v1);
        Objects.requireNonNull(v2);
        if (v1.equals(v2)) {
            throw new IllegalArgumentException("Identity edges are not permitted: " + v1 + " to " + v2);
        }
        if (!vertices.contains(v1)) {
            throw new IllegalArgumentException("One of edge vertices does not belong to graph: " + v1);
        }
        if (!vertices.contains(v2)) {
            throw new IllegalArgumentException("One of edge vertices does not belong to graph: " + v2);
        }
        DirectedEdge edge = new DirectedEdge(v1, v2);
        if (!edges.add(edge)) {
            throw new IllegalArgumentException("Edge is already in graph: " + v1 + " -> " + v2);
        }
    }
}
