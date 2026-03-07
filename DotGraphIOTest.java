package edu.asu.cse464.dot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

final class DotGraphIOTest {
  @TempDir
  Path tempDir;

  @Test
  void feature1_parseGraph_and_outputGraph() throws Exception {
    Path dot = tempDir.resolve("input.dot");
    Files.writeString(dot, """
        digraph G {
          a -> b;
          b -> c;
          a;
          "node x" -> c;
        }
        """, StandardCharsets.UTF_8);

    DirectedGraph g = DotGraphIO.parseGraph(dot.toString());

    String printed = g.toString();
    assertTrue(printed.contains("Nodes (4):"));
    assertTrue(printed.contains("a -> b"));
    assertTrue(printed.contains("b -> c"));
    assertTrue(printed.contains("node x -> c"));

    Path out = tempDir.resolve("graph.txt");
    DotGraphIO.outputGraph(out.toString(), g);
    assertTrue(Files.exists(out));
    assertTrue(Files.size(out) > 0);
  }
}