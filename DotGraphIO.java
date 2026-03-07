package edu.asu.cse464.dot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class DotGraphIO {
  private DotGraphIO() {}

  public static DirectedGraph parseGraph(String filepath) throws IOException {
    String dot = Files.readString(Path.of(filepath), StandardCharsets.UTF_8);
    return DotParser.parse(dot);
  }

  public static void outputGraph(String filepath, DirectedGraph graph) throws IOException {
    Path out = Path.of(filepath);
    Path parent = out.toAbsolutePath().getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }
    Files.writeString(out, graph.toString(), StandardCharsets.UTF_8);
  }
}