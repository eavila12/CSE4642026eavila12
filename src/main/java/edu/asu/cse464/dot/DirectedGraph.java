package edu.asu.cse464.dot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
    if (!nodes.contains(label)) {
      throw new IllegalArgumentException("Node does not exist: " + label);
    }

    nodes.remove(label);
    edges.removeIf(e -> e.src().equals(label) || e.dst().equals(label));
  }

  public void removeNodes(String[] labels) {
    Objects.requireNonNull(labels, "labels");

    for (String label : labels) {
      validateLabel(label);
      if (!nodes.contains(label)) {
        throw new IllegalArgumentException("Node does not exist: " + label);
      }
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
    if (!nodes.contains(label)) {
      throw new IllegalArgumentException("Node does not exist: " + label);
    }
    return new Node(label);
  }

  public Path GraphSearch(Node src, Node dst) {
    Objects.requireNonNull(src, "src");
    Objects.requireNonNull(dst, "dst");

    if (!nodes.contains(src.label()) || !nodes.contains(dst.label())) {
      throw new IllegalArgumentException("Both nodes must exist in graph.");
    }

    Set<String> visited = new HashSet<>();
    Map<String, String> parent = new HashMap<>();

    boolean found = dfs(src.label(), dst.label(), visited, parent);
    if (!found) {
      return null;
    }

    return buildPath(parent, src.label(), dst.label());
  }

  private boolean dfs(String current, String target, Set<String> visited, Map<String, String> parent) {
    visited.add(current);

    if (current.equals(target)) {
      return true;
    }

    for (String neighbor : getNeighbors(current)) {
      if (!visited.contains(neighbor)) {
        parent.put(neighbor, current);
        if (dfs(neighbor, target, visited, parent)) {
          return true;
        }
      }
    }

    return false;
  }

  private Path buildPath(Map<String, String> parent, String src, String dst) {
    List<Node> pathNodes = new ArrayList<>();
    String current = dst;

    while (current != null) {
      pathNodes.add(new Node(current));
      if (current.equals(src)) {
        break;
      }
      current = parent.get(current);
    }

    Collections.reverse(pathNodes);

    if (pathNodes.isEmpty() || !pathNodes.get(0).label().equals(src)) {
      return null;
    }

    return new Path(pathNodes);
  }

  private List<String> getNeighbors(String label) {
    List<String> neighbors = new ArrayList<>();
    for (DirectedEdge edge : edges) {
      if (edge.src().equals(label)) {
        neighbors.add(edge.dst());
      }
    }
    return neighbors;
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
    if (simple) {
      return id;
    }
    String escaped = id.replace("\\", "\\\\").replace("\"", "\\\"");
    return "\"" + escaped + "\"";
  }
}