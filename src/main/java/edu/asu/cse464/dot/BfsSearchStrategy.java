package edu.asu.cse464.dot;

final class BfsSearchStrategy implements GraphSearchStrategy {
    private final BfsTemplateSearch template;

    BfsSearchStrategy(DirectedGraph graph) {
        this.template = new BfsTemplateSearch(graph);
    }

    @Override
    public Path search(Node src, Node dst) {
        return template.search(src, dst);
    }
}