package edu.asu.cse464.dot;

final class DfsSearchStrategy implements GraphSearchStrategy {
    private final DfsTemplateSearch template;

    DfsSearchStrategy(DirectedGraph graph) {
        this.template = new DfsTemplateSearch(graph);
    }

    @Override
    public Path search(Node src, Node dst) {
        return template.search(src, dst);
    }
}