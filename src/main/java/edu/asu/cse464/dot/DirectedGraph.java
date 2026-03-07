package edu.asu.cse464.dot;

import java.util.Collections;
import java.util.LinkedHashSet;
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
    for (DirectedEdge e : edges) {
      sb.append("  ").append(e.src()).append(" -> ").append(e.dst()).append(System.lineSeparator());
    }
    return sb.toString();
  }

  public String toDot() {
    StringBuilder sb = new StringBuilder();
    sb.append("digraph G {").append(System.lineSeparator());
    for (String n : nodes) {
      sb.append("  ").append(escapeId(n)).append(";").append(System.lineSeparator());
    }
    for (DirectedEdge e : edges) {
      sb.append("  ").append(escapeId(e.src())).append(" -> ").append(escapeId(e.dst())).append(";")
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
    if (simple) return id;
    String escaped = id.replace("\\", "\\\\").replace("\"", "\\\"");
    return "\"" + escaped + "\"";
  }
}