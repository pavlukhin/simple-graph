package org.ivan.examples.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

// t0d0 generalize vertex type
// t0d0 undirected graph support
public class Graph {
    public interface Edge {
        Vertex from();
        Vertex to();
    }

    private static class EdgeImpl implements Edge {
        private final Vertex from;
        private final Vertex to;

        private EdgeImpl(Vertex from, Vertex to) {
            this.from = Objects.requireNonNull(from);
            this.to = Objects.requireNonNull(to);
        }

        @Override public Vertex from() {
            return from;
        }

        @Override public Vertex to() {
            return to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            EdgeImpl edge = (EdgeImpl)o;
            return Objects.equals(from, edge.from) &&
                Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }

    private final Map<Vertex, Set<Vertex>> adjacencyMap = new HashMap<>();
    private final Set<Vertex> vertices = adjacencyMap.keySet();

    public void addVertex(Vertex v) {
        Objects.requireNonNull(v);
        if (vertices.contains(v)) {
            // t0d0 proof-read exception messages
            throw new IllegalArgumentException("Vertex is already in graph: " + v);
        }
        adjacencyMap.put(v, new HashSet<>());
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
        Set<Vertex> adjs = adjacencyMap.get(v1);
        if (!adjs.add(v2)) {
            throw new IllegalArgumentException("Edge is already in graph: " + v1 + " -> " + v2);
        }
    }

    // t0d0 consider something custom instead of standard Java Optional
    public Optional<List<Edge>> getPath(Vertex from, Vertex to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (!vertices.contains(from)) {
            throw new IllegalArgumentException("Vertex does not belong to graph: " + from);
        }
        if (!vertices.contains(to)) {
            throw new IllegalArgumentException("Vertex does not belong to graph: " + to);
        }

        if (from.equals(to)) {
            return Optional.of(Collections.emptyList());
        }
        // t0d0 Analyze complexity
        LinkedList<Edge> path = new LinkedList<>();
        Set<Vertex> visitedVertices = new HashSet<>();
        if (advancePath(from, to, path, visitedVertices)) {
            return Optional.of(path);
        }
        return Optional.empty();
    }

    private boolean advancePath(Vertex from, Vertex to, LinkedList<Edge> path, Set<Vertex> visitedVertices) {
        visitedVertices.add(from);

        Set<Vertex> adjs = adjacencyMap.get(from);
        if (adjs.contains(to)) {
            path.addLast(new EdgeImpl(from, to));
            return true;
        }
        // advance further
        for (Vertex v : adjs) {
            path.addLast(new EdgeImpl(from, v));
            if (!visitedVertices.contains(v) && advancePath(v, to, path, visitedVertices)) {
                return true;
            }
            path.removeLast();
        }
        return false;
    }
}
