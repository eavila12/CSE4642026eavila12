package edu.asu.cse464.dot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class Path {
  private final List<Node> nodes;

  public Path(List<Node> nodes) {
    if (nodes == null || nodes.isEmpty()) {
      throw new IllegalArgumentException("Path must contain at least one node.");
    }
    this.nodes = Collections.unmodifiableList(new ArrayList<>(nodes));
  }

  public List<Node> getNodes() {
    return nodes;
  }

  @Override
  public String toString() {
    return nodes.stream().map(Node::label).collect(Collectors.joining(" -> "));
  }
}