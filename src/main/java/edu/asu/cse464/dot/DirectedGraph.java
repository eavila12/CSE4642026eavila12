package edu.asu.cse464.dot;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class DirectedGraph {
    private final LinkedHashSet<String> nodes = new LinkedHashSet<>();
    private final LinkedHashSet<DirectedEdge> edges = new LinkedHashSet<>();

    public boolean addNode(String label) {
        validateLabel(label);
        return nodes.add(label);
    }

    public int addNodes(String[] labels) {
        Objects.requireNonNull(labels, "labels");
        int added = 0;
        for (String label : labels) {
            if (addNode(label)) {
                added++;
            }
        }
        return added;
    }

    public boolean addEdge(String srcLabel, String dstLabel) {
        validateLabel(srcLabel);
        validateLabel(dstLabel);
        nodes.add(srcLabel);
        nodes.add(dstLabel);
        return edges.add(new DirectedEdge(srcLabel, dstLabel));
    }

    public void removeNode(String label) {
        validateLabel(label);
        requireNodeExists(label);
        nodes.remove(label);
        edges.removeIf(edge -> edge.src().equals(label) || edge.dst().equals(label));
    }

    public void removeNodes(String[] labels) {
        Objects.requireNonNull(labels, "labels");
        for (String label : labels) {
            validateLabel(label);
            requireNodeExists(label);
        }
        for (String label : labels) {
            removeNode(label);
        }
    }

    public void removeEdge(String srcLabel, String dstLabel) {
        validateLabel(srcLabel);
        validateLabel(dstLabel);
        DirectedEdge edge = new DirectedEdge(srcLabel, dstLabel);
        if (!edges.contains(edge)) {
            throw new IllegalArgumentException("Edge does not exist: " + srcLabel + " -> " + dstLabel);
        }
        edges.remove(edge);
    }

    public Node getNode(String label) {
        validateLabel(label);
        requireNodeExists(label);
        return new Node(label);
    }

    public boolean containsNode(String label) {
        return nodes.contains(label);
    }

    public void requireSearchEndpoints(Node src, Node dst) {
        Objects.requireNonNull(src, "src");
        Objects.requireNonNull(dst, "dst");
        if (!containsNode(src.label()) || !containsNode(dst.label())) {
            throw new IllegalArgumentException("Both nodes must exist in graph.");
        }
    }

    public List<String> getNeighbors(String label) {
        validateLabel(label);
        List<String> neighbors = new ArrayList<>();
        for (DirectedEdge edge : edges) {
            if (edge.src().equals(label)) {
                neighbors.add(edge.dst());
            }
        }
        return neighbors;
    }

    public Path buildPath(java.util.Map<String, String> parent, String src, String dst) {
        Deque<Node> reversed = new ArrayDeque<>();
        String current = dst;
        while (current != null) {
            reversed.push(new Node(current));
            if (current.equals(src)) {
                return new Path(new ArrayList<>(reversed));
            }
            current = parent.get(current);
        }
        return null;
    }

    private void requireNodeExists(String label) {
        if (!nodes.contains(label)) {
            throw new IllegalArgumentException("Node does not exist: " + label);
        }
    }

    public Set<String> getNodes() {
        return Collections.unmodifiableSet(nodes);
    }

    public Set<DirectedEdge> getEdges() {
        return Collections.unmodifiableSet(edges);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nodes (").append(nodes.size()).append("): ").append(nodes).append(System.lineSeparator());
        sb.append("Edges (").append(edges.size()).append("):").append(System.lineSeparator());
        for (DirectedEdge edge : edges) {
            sb.append(" ").append(edge.src()).append(" -> ").append(edge.dst()).append(System.lineSeparator());
        }
        return sb.toString();
    }

    public String toDot() {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {").append(System.lineSeparator());
        for (String node : nodes) {
            sb.append(" ").append(escapeId(node)).append(";").append(System.lineSeparator());
        }
        for (DirectedEdge edge : edges) {
            sb.append(" ")
                .append(escapeId(edge.src()))
                .append(" -> ")
                .append(escapeId(edge.dst()))
                .append(";")
                .append(System.lineSeparator());
        }
        sb.append("}").append(System.lineSeparator());
        return sb.toString();
    }

    private static void validateLabel(String label) {
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Node label must be non-empty.");
        }
    }

    private static String escapeId(String id) {
        boolean simple = id.matches("[A-Za-z_][A-Za-z0-9_]*");
        if (simple) {
            return id;
        }
        String escaped = id.replace("\\", "\\\\").replace("\"", "\\\"");
        return "\"" + escaped + "\"";
    }
}