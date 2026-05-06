package edu.asu.cse464.dot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class DotGraphIOTest {
    @TempDir
    Path tempDir;

    @Test
void graph_search_bfs_finds_a_path() {
    DirectedGraph g = new DirectedGraph();
    g.addEdge("a", "b");
    g.addEdge("a", "c");
    g.addEdge("b", "d");
    g.addEdge("c", "d");

    edu.asu.cse464.dot.Path path = g.GraphSearch(g.getNode("a"), g.getNode("d"), Algorithm.BFS);

    assertNotNull(path);
    assertEquals("a -> b -> d", path.toString());
}

@Test
void graph_search_dfs_finds_a_path() {
    DirectedGraph g = new DirectedGraph();
    g.addEdge("a", "b");
    g.addEdge("b", "d");
    g.addEdge("a", "c");

    edu.asu.cse464.dot.Path path = g.GraphSearch(g.getNode("a"), g.getNode("d"), Algorithm.DFS);

    assertNotNull(path);
    assertEquals("a -> b -> d", path.toString());
}

@Test
void graph_search_random_walk_can_find_a_to_c() {
    DirectedGraph g = new DirectedGraph();
    g.addEdge("a", "b");
    g.addEdge("a", "e");
    g.addEdge("b", "c");
    g.addEdge("e", "f");
    g.addEdge("e", "g");
    g.addEdge("g", "h");

    boolean found = false;
    for (int i = 0; i < 25; i++) {
        edu.asu.cse464.dot.Path path = g.GraphSearch(g.getNode("a"), g.getNode("c"), Algorithm.RANDOM_WALK);
        if (path != null && path.toString().endsWith("c")) {
            found = true;
            break;
        }
    }

    assertTrue(found);
}

@Test
void graph_search_bfs_finds_a_to_h_path() {
    DirectedGraph g = new DirectedGraph();
    g.addEdge("a", "b");
    g.addEdge("b", "c");
    g.addEdge("c", "d");
    g.addEdge("d", "a");
    g.addEdge("a", "e");
    g.addEdge("e", "f");
    g.addEdge("e", "g");
    g.addEdge("f", "h");
    g.addEdge("g", "h");

    edu.asu.cse464.dot.Path path =
        g.GraphSearch(g.getNode("a"), g.getNode("h"), Algorithm.BFS);

    assertNotNull(path);
    assertTrue(path.toString().equals("a -> e -> f -> h")
            || path.toString().equals("a -> e -> g -> h"));
}

@Test
void graph_search_random_walk_can_find_a_to_h() {
    DirectedGraph g = new DirectedGraph();
    g.addEdge("a", "b");
    g.addEdge("b", "c");
    g.addEdge("c", "d");
    g.addEdge("d", "a");
    g.addEdge("a", "e");
    g.addEdge("e", "f");
    g.addEdge("e", "g");
    g.addEdge("f", "h");
    g.addEdge("g", "h");

    boolean found = false;
    for (int i = 0; i < 25; i++) {
        edu.asu.cse464.dot.Path path = g.GraphSearch(g.getNode("a"), g.getNode("h"), Algorithm.RANDOM_WALK);
        if (path != null && path.toString().endsWith("h")) {
            found = true;
            break;
        }
    }

    assertTrue(found);
}

@Test
void graph_search_dfs_finds_a_to_h_path() {
    DirectedGraph g = new DirectedGraph();
    g.addEdge("a", "b");
    g.addEdge("b", "c");
    g.addEdge("c", "d");
    g.addEdge("d", "a");
    g.addEdge("a", "e");
    g.addEdge("e", "f");
    g.addEdge("e", "g");
    g.addEdge("f", "h");
    g.addEdge("g", "h");

    edu.asu.cse464.dot.Path path = g.GraphSearch(g.getNode("a"), g.getNode("h"), Algorithm.DFS);

    assertNotNull(path);
    assertTrue(path.toString().endsWith("h"));
}
}