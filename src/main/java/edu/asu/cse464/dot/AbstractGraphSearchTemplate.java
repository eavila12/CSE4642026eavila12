package edu.asu.cse464.dot;

import java.util.List;

abstract class AbstractGraphSearchTemplate {
    protected final DirectedGraph graph;

    protected AbstractGraphSearchTemplate(DirectedGraph graph) {
        this.graph = graph;
    }

    public final Path search(Node src, Node dst) {
        graph.requireSearchEndpoints(src, dst);

        SearchState state = new SearchState();
        Frontier<String> frontier = createFrontier();

        initialize(frontier, state, src.label());

        while (!frontier.isEmpty()) {
            String current = removeNext(frontier);

            if (isDestination(current, dst.label())) {
                return graph.buildPath(state.parentMap(), src.label(), dst.label());
            }

            for (String neighbor : orderedNeighbors(current)) {
                if (shouldVisit(neighbor, state)) {
                    state.setParent(neighbor, current);
                    onDiscover(frontier, state, neighbor);
                }
            }
        }

        return null;
    }

    protected void initialize(Frontier<String> frontier, SearchState state, String src) {
        state.markVisited(src);
        frontier.add(src);
    }

    protected String removeNext(Frontier<String> frontier) {
        return frontier.remove();
    }

    protected boolean isDestination(String current, String dst) {
        return current.equals(dst);
    }

    protected boolean shouldVisit(String neighbor, SearchState state) {
        return !state.hasVisited(neighbor);
    }

    protected void onDiscover(Frontier<String> frontier, SearchState state, String neighbor) {
        state.markVisited(neighbor);
        frontier.add(neighbor);
    }

    protected List<String> orderedNeighbors(String current) {
        return graph.getNeighbors(current);
    }

    protected abstract Frontier<String> createFrontier();
}