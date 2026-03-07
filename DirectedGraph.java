package edu.asu.cse464.dot;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public final class DirectedGraph {
  private final LinkedHashSet<String> nodes = new LinkedHashSet<>();
  private final LinkedHashMap<String, LinkedHashSet<String>> edges = new LinkedHashMap<>();

  void addNodeInternal(String label) {
    nodes.add(label);
  }

  void addEdgeInternal(String src, String dst) {
    nodes.add(src);
    nodes.add(dst);
    edges.computeIfAbsent(src, k -> new LinkedHashSet<>()).add(dst);
  }

  public Set<String> getNodes() {
    return Collections.unmodifiableSet(nodes);
  }

  public Map<String, Set<String>> getAdjacency() {
    Map<String, Set<String>> out = new LinkedHashMap<>();
    for (Map.Entry<String, LinkedHashSet<String>> e : edges.entrySet()) {
      out.put(e.getKey(), Collections.unmodifiableSet(e.getValue()));
    }
    return Collections.unmodifiableMap(out);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Nodes (").append(nodes.size()).append("): ").append(nodes).append(System.lineSeparator());
    int edgeCount = 0;
    for (LinkedHashSet<String> dsts : edges.values()) {
      edgeCount += dsts.size();
    }
    sb.append("Edges (").append(edgeCount).append("):").append(System.lineSeparator());
    for (Map.Entry<String, LinkedHashSet<String>> e : edges.entrySet()) {
      for (String dst : e.getValue()) {
        sb.append("  ").append(e.getKey()).append(" -> ").append(dst).append(System.lineSeparator());
      }
    }
    return sb.toString();
  }
}