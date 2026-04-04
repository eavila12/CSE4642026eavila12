package edu.asu.cse464.dot;

import java.util.Objects;

public record Node(String label) {
  public Node {
    Objects.requireNonNull(label, "label");
    if (label.isBlank()) {
      throw new IllegalArgumentException("label must be non-empty");
    }
  }

  @Override
  public String toString() {
    return label;
  }
}