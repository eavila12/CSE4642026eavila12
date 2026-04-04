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

    assertEquals(4, g.getNodes().size());
    assertEquals(3, g.getEdges().size());

    Path out = tempDir.resolve("graph.txt");
    DotGraphIO.outputGraph(out.toString(), g);
    assertTrue(Files.exists(out));
    assertTrue(Files.size(out) > 0);
  }

  @Test
  void feature2_addNode_addNodes_duplicateChecks() {
    DirectedGraph g = new DirectedGraph();

    assertTrue(g.addNode("a"));
    assertFalse(g.addNode("a"));

    int added = g.addNodes(new String[] {"a", "b", "b", "c"});
    assertEquals(2, added);
    assertEquals(3, g.getNodes().size());
  }

  @Test
  void feature3_addEdge_duplicateChecks_and_autoNodeCreate() {
    DirectedGraph g = new DirectedGraph();

    assertTrue(g.addEdge("a", "b"));
    assertFalse(g.addEdge("a", "b"));

    assertTrue(g.getNodes().contains("a"));
    assertTrue(g.getNodes().contains("b"));
    assertEquals(1, g.getEdges().size());
  }

  @Test
  void feature4_outputDot_and_png() throws Exception {
    DirectedGraph g = new DirectedGraph();
    g.addEdge("a", "b");
    g.addEdge("b", "c");

    Path dotOut = tempDir.resolve("out.dot");
    DotGraphIO.outputDOTGraph(dotOut.toString(), g);
    String dot = Files.readString(dotOut, StandardCharsets.UTF_8);
    assertTrue(dot.startsWith("digraph G {"));
    assertTrue(dot.contains("a -> b;"));

    Path pngOut = tempDir.resolve("out.png");
    DotGraphIO.outputGraphics(pngOut.toString(), "png", g);
    assertTrue(Files.exists(pngOut));
    assertTrue(Files.size(pngOut) > 0);
  }

  @Test
  void part2_remove_some_nodes_and_edges_successfully() {
    DirectedGraph g = new DirectedGraph();
    g.addEdge("a", "b");
    g.addEdge("b", "c");
    g.addEdge("a", "c");
    g.addNode("d");

    g.removeEdge("a", "c");
    g.removeNode("b");

    assertFalse(g.getEdges().contains(new DirectedEdge("a", "c")));
    assertFalse(g.getNodes().contains("b"));
    assertFalse(g.getEdges().contains(new DirectedEdge("a", "b")));
    assertFalse(g.getEdges().contains(new DirectedEdge("b", "c")));
    assertTrue(g.getNodes().contains("a"));
    assertTrue(g.getNodes().contains("c"));
    assertTrue(g.getNodes().contains("d"));
  }

  @Test
  void part2_remove_nodes_that_do_not_exist_should_throw() {
    DirectedGraph g = new DirectedGraph();
    g.addNode("a");
    g.addNode("b");

    assertThrows(IllegalArgumentException.class, () -> g.removeNode("z"));
    assertThrows(IllegalArgumentException.class, () -> g.removeNodes(new String[]{"a", "z"}));
  }

  @Test
  void part2_remove_edges_that_do_not_exist_should_throw() {
    DirectedGraph g = new DirectedGraph();
    g.addNode("a");
    g.addNode("b");

    assertThrows(IllegalArgumentException.class, () -> g.removeEdge("a", "b"));
    assertThrows(IllegalArgumentException.class, () -> g.removeEdge("x", "y"));
  }

  @Test
  void dfs_graph_search_finds_a_path() {
    DirectedGraph g = new DirectedGraph();
    g.addEdge("a", "b");
    g.addEdge("b", "d");
    g.addEdge("a", "c");

    Path path = g.GraphSearch(g.getNode("a"), g.getNode("d"));

    assertNotNull(path);
    assertEquals("a -> b -> d", path.toString());
  }

  @Test
  void dfs_graph_search_returns_null_when_unreachable() {
    DirectedGraph g = new DirectedGraph();
    g.addEdge("a", "b");
    g.addNode("z");

    Path path = g.GraphSearch(g.getNode("a"), g.getNode("z"));

    assertNull(path);
  }
}