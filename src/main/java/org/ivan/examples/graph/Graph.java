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

public class Graph<V> {
    public interface Edge<V> {
        V from();
        V to();
    }

    private static class EdgeImpl<V> implements Edge<V> {
        private final V from;
        private final V to;

        private EdgeImpl(V from, V to) {
            this.from = Objects.requireNonNull(from);
            this.to = Objects.requireNonNull(to);
        }

        @Override public V from() {
            return from;
        }

        @Override public V to() {
            return to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            @SuppressWarnings("rawtypes")
            EdgeImpl edge = (EdgeImpl)o;
            return Objects.equals(from, edge.from) &&
                Objects.equals(to, edge.to);
        }

        @Override
        public int hashCode() {
            return Objects.hash(from, to);
        }
    }

    private final boolean isDirected;
    private final Map<V, Set<V>> adjacencyMap = new HashMap<>();
    private final Set<V> vertices = adjacencyMap.keySet();

    public static <V> Graph<V> newDirectedGraph() {
        return new Graph<>(true);
    }

    public static <V> Graph<V> newUndirectedGraph() {
        return new Graph<>(false);
    }

    private Graph(boolean isDirected) {
        this.isDirected = isDirected;
    }

    public void addVertex(V v) {
        Objects.requireNonNull(v);
        if (vertices.contains(v)) {
            throw new IllegalArgumentException("A vertex is already in the graph: " + v);
        }
        adjacencyMap.put(v, new HashSet<>());
    }

    public void addEdge(V v1, V v2) {
        Objects.requireNonNull(v1);
        Objects.requireNonNull(v2);
        if (v1.equals(v2)) {
            throw new IllegalArgumentException("Identity edges are not permitted: " + v1 + " -> " + v2);
        }
        if (!vertices.contains(v1)) {
            throw new IllegalArgumentException("One of edge vertices does not belong to the graph: " + v1);
        }
        if (!vertices.contains(v2)) {
            throw new IllegalArgumentException("One of edge vertices does not belong to the graph: " + v2);
        }
        Set<V> adjs = adjacencyMap.get(v1);
        if (!adjs.add(v2)) {
            throw new IllegalArgumentException("An edge is already in the graph: " + v1 + " -> " + v2);
        }
        if (!isDirected) {
            adjacencyMap.get(v2).add(v1);
        }
    }

    // t0d0 consider something custom instead of standard Java Optional
    public Optional<List<Edge<V>>> getPath(V from, V to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (!vertices.contains(from)) {
            throw new IllegalArgumentException("A vertex does not belong to the graph: " + from);
        }
        if (!vertices.contains(to)) {
            throw new IllegalArgumentException("A vertex does not belong to the graph: " + to);
        }

        if (from.equals(to)) {
            return Optional.of(Collections.emptyList());
        }
        // t0d0 Analyze complexity
        LinkedList<Edge<V>> path = new LinkedList<>();
        Set<V> visitedVertices = new HashSet<>();
        if (advancePath(from, to, path, visitedVertices)) {
            return Optional.of(path);
        }
        return Optional.empty();
    }

    private boolean advancePath(V from, V to, LinkedList<Edge<V>> path, Set<V> visitedVertices) {
        visitedVertices.add(from);

        Set<V> adjs = adjacencyMap.get(from);
        if (adjs.contains(to)) {
            path.addLast(new EdgeImpl<>(from, to));
            return true;
        }
        // advance further
        for (V v : adjs) {
            path.addLast(new EdgeImpl<>(from, v));
            if (!visitedVertices.contains(v) && advancePath(v, to, path, visitedVertices)) {
                return true;
            }
            path.removeLast();
        }
        return false;
    }
}
