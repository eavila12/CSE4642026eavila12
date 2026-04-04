package edu.asu.cse464.dot;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
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

<<<<<<< Updated upstream
=======
  public Node getNode(String label) {
    validateLabel(label);
    if (!nodes.contains(label)) {
      throw new IllegalArgumentException("Node does not exist: " + label);
    }
    return new Node(label);
  }

  public Path GraphSearch(Node src, Node dst, Algorithm algo) {
    Objects.requireNonNull(src, "src");
    Objects.requireNonNull(dst, "dst");
    Objects.requireNonNull(algo, "algo");

    if (!nodes.contains(src.label()) || !nodes.contains(dst.label())) {
      throw new IllegalArgumentException("Both nodes must exist in graph.");
    }

    return switch (algo) {
      case BFS -> bfs(src.label(), dst.label());
      case DFS -> dfsSearch(src.label(), dst.label());
    };
  }

  private Path bfs(String src, String dst) {
    Queue<String> queue = new ArrayDeque<>();
    Set<String> visited = new HashSet<>();
    Map<String, String> parent = new HashMap<>();

    queue.add(src);
    visited.add(src);

    while (!queue.isEmpty()) {
      String current = queue.remove();
      if (current.equals(dst)) {
        return buildPath(parent, src, dst);
      }

      for (String neighbor : getNeighbors(current)) {
        if (!visited.contains(neighbor)) {
          visited.add(neighbor);
          parent.put(neighbor, current);
          queue.add(neighbor);
        }
      }
    }

    return null;
  }

  private Path dfsSearch(String src, String dst) {
    Set<String> visited = new HashSet<>();
    Map<String, String> parent = new HashMap<>();

    boolean found = dfs(src, dst, visited, parent);
    if (!found) {
      return null;
    }

    return buildPath(parent, src, dst);
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

  private List<String> getNeighbors(String label) {
    List<String> neighbors = new ArrayList<>();
    for (DirectedEdge edge : edges) {
      if (edge.src().equals(label)) {
        neighbors.add(edge.dst());
      }
    }
    return neighbors;
  }

>>>>>>> Stashed changes
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