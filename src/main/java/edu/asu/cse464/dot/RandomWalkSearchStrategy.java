package edu.asu.cse464.dot;

final class RandomWalkSearchStrategy implements GraphSearchStrategy {
    private final RandomWalkTemplateSearch template;

    RandomWalkSearchStrategy(DirectedGraph graph) {
        this.template = new RandomWalkTemplateSearch(graph);
    }

    @Override
    public Path search(Node src, Node dst) {
        return template.search(src, dst);
    }
}