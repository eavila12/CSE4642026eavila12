package edu.asu.cse464.dot;

import java.util.Objects;

public record DirectedEdge(String src, String dst) {
  public DirectedEdge {
    Objects.requireNonNull(src, "src");
    Objects.requireNonNull(dst, "dst");
  }
}