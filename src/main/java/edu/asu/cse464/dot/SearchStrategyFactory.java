package edu.asu.cse464.dot;

final class SearchStrategyFactory {
    private SearchStrategyFactory() {
    }

    static GraphSearchStrategy create(DirectedGraph graph, Algorithm algorithm) {
        return switch (algorithm) {
            case BFS -> new BfsSearchStrategy(graph);
            case DFS -> new DfsSearchStrategy(graph);
            case RANDOM_WALK -> new RandomWalkSearchStrategy(graph);
        };
    }
}