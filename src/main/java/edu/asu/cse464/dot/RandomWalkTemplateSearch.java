package edu.asu.cse464.dot;

import java.util.List;
import java.util.Random;

final class RandomWalkTemplateSearch extends AbstractGraphSearchTemplate {
    private final Random random;

    RandomWalkTemplateSearch(DirectedGraph graph) {
        this(graph, new Random());
    }

    RandomWalkTemplateSearch(DirectedGraph graph, Random random) {
        super(graph);
        this.random = random;
    }

    @Override
    protected Frontier<String> createFrontier() {
        return new RandomFrontier<>(random);
    }

    @Override
    protected List<String> orderedNeighbors(String current) {
        return graph.getNeighbors(current);
    }
}