package edu.asu.cse464.dot;

final class BfsTemplateSearch extends AbstractGraphSearchTemplate {
    BfsTemplateSearch(DirectedGraph graph) {
        super(graph);
    }

    @Override
    protected Frontier<String> createFrontier() {
        return new QueueFrontier<>();
    }
}