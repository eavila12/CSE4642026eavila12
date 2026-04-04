  public Path GraphSearch(Node src, Node dst) {
    Objects.requireNonNull(src, "src");
    Objects.requireNonNull(dst, "dst");

    if (!nodes.contains(src.label()) || !nodes.contains(dst.label())) {
      throw new IllegalArgumentException("Both nodes must exist in graph.");
    }

    Set<String> visited = new HashSet<>();
    Map<String, String> parent = new HashMap<>();

    boolean found = dfs(src.label(), dst.label(), visited, parent);
    if (!found) {
      return null;
    }

    return buildPath(parent, src.label(), dst.label());
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