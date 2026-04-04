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