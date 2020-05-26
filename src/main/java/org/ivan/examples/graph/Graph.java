package org.ivan.examples.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a graph (vertices and edges connecting pairs of vertices).<p>
 *
 * A graph can be configured as either directed or undirected upon creation.<p>
 *
 * This class is parameterized with a vertex type.
 * Internally methods {@link Object#equals} and {@link Object#hashCode()} are used for determining vertex equality
 * (default implementations inherited from {@link Object} will work fine).<p>
 *
 * API is designed in a <i>strict</i> fashion in order to prevent programming errors early.
 * {@code null} vertices are prohibited.
 * @param <V> a vertex type
 */
public final class Graph<V> {
    public interface Edge<V> {
        V from();
        V to();
    }

    public interface Path<V> {
        /**
         * @return {@code true} if the requested path exists
         */
        boolean exists();

        /**
         * Returns edges of the existing path (in terms of {@link #exists()}).
         * It is illegal to call this method when the path does not exist.
         * @return list of existing path edges
         */
        List<Edge<V>> edges();
    }

    private static class EdgeImpl<V> implements Edge<V> {
        private final V from;
        private final V to;

        private EdgeImpl(V from, V to) {
            this.from = Objects.requireNonNull(from);
            this.to = Objects.requireNonNull(to);
        }

        @Override
        public V from() {
            return from;
        }

        @Override
        public V to() {
            return to;
        }
    }

    private static class PathImpl<V> implements Path<V> {
        private final List<Edge<V>> edges;

        private PathImpl(List<Edge<V>> edges) {
            this.edges = edges;
        }

        @Override
        public boolean exists() {
            return edges != null;
        }

        @Override
        public List<Edge<V>> edges() {
            if (!exists()) {
                throw new IllegalStateException("This path does not exist.");
            }
            return edges;
        }
    }

    private final boolean isDirected;
    private final Map<V, Set<V>> adjacencyMap = new HashMap<>();
    private final Set<V> vertices = adjacencyMap.keySet();

    /**
     * Creates a directed graph.
     * @param <V> a vertex type.
     * @return new directed graph.
     */
    public static <V> Graph<V> newDirectedGraph() {
        return new Graph<>(true);
    }

    /**
     * Creates an undirected graph.
     * @param <V> a vertex type.
     * @return new undirected graph.
     */
    public static <V> Graph<V> newUndirectedGraph() {
        return new Graph<>(false);
    }

    // private to prevent direct usage, static factory methods are supposed to be used
    private Graph(boolean isDirected) {
        this.isDirected = isDirected;
    }

    /**
     * Adds a vertex to the graph.
     * <ul>
     *     <li>It is illegal to add a vertex which is already in the graph.</li>
     * </ul>
     * @param v a vertex to add ({@code null} is prohibited)
     */
    public void addVertex(V v) {
        Objects.requireNonNull(v);
        if (vertices.contains(v)) {
            throw new IllegalArgumentException("A vertex is already in the graph: " + v);
        }
        adjacencyMap.put(v, new HashSet<>());
    }

    /**
     * Adds an edge to the graph.
     * <ul>
     *     <li>It is required that both edge vertices was already added to the graph.</li>
     *     <li>Identity edges (start and end vertices are equal) are treated invalid.</li>
     *     <li>It is illegal to add an edge which is already in the graph.</li>
     * </ul>
     * @param from an edge start vertex ({@code null} is prohibited)
     * @param to an edge end vertex ({@code null} is prohibited)
     */
    public void addEdge(V from, V to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (from.equals(to)) {
            throw new IllegalArgumentException("Identity edges are not permitted: " + from + " -> " + to);
        }
        if (!vertices.contains(from)) {
            throw new IllegalArgumentException("One of edge vertices does not belong to the graph: " + from);
        }
        if (!vertices.contains(to)) {
            throw new IllegalArgumentException("One of edge vertices does not belong to the graph: " + to);
        }
        Set<V> adjs = adjacencyMap.get(from);
        if (!adjs.add(to)) {
            throw new IllegalArgumentException("An edge is already in the graph: " + from + " -> " + to);
        }
        if (!isDirected) {
            adjacencyMap.get(to).add(from);
        }
    }

    /**
     * Searches a path between 2 vertices in the graph.
     * <ul>
     *     <li>It is required that both vertices was already added to the graph.</li>
     * </ul>
     * @param from a path start vertex ({@code null} is prohibited)
     * @param to a path end vertex ({@code null} is prohibited)
     * @return A path (possibly not existing). An empty list of edges means that both vertices are equal.
     */
    public Path<V> getPath(V from, V to) {
        Objects.requireNonNull(from);
        Objects.requireNonNull(to);
        if (!vertices.contains(from)) {
            throw new IllegalArgumentException("A vertex does not belong to the graph: " + from);
        }
        if (!vertices.contains(to)) {
            throw new IllegalArgumentException("A vertex does not belong to the graph: " + to);
        }

        if (from.equals(to)) {
            return new PathImpl<>(Collections.emptyList());
        }
        // t0d0 Analyze complexity
        LinkedList<Edge<V>> path = new LinkedList<>();
        Set<V> visitedVertices = new HashSet<>();
        if (advancePath(from, to, path, visitedVertices)) {
            return new PathImpl<>(path);
        }
        return new PathImpl<>(null);
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
