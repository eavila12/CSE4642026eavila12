package edu.asu.cse464.dot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

final class SearchState {
    private final Set<String> visited = new HashSet<>();
    private final Map<String, String> parent = new HashMap<>();

    boolean markVisited(String node) {
        return visited.add(node);
    }

    boolean hasVisited(String node) {
        return visited.contains(node);
    }

    void setParent(String child, String parentNode) {
        parent.put(child, parentNode);
    }

    Map<String, String> parentMap() {
        return parent;
    }
}