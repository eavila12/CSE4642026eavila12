package edu.asu.cse464.dot;

import java.nio.file.Path;

public final class App {
    private App() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Usage: java edu.asu.cse464.dot.App <input.dot> <outDir>");
            System.exit(2);
        }

        Path input = Path.of(args[0]);
        Path outDir = Path.of(args[1]);

        DirectedGraph graph = DotGraphIO.parseGraph(input.toString());

        System.out.println(graph);
        DotGraphIO.outputGraph(outDir.resolve("graph.txt").toString(), graph);
        DotGraphIO.outputDOTGraph(outDir.resolve("graph.dot").toString(), graph);
        DotGraphIO.outputGraphics(outDir.resolve("graph.png").toString(), "png", graph);

        runSearches(graph);
    }

    private static void runSearches(DirectedGraph graph) {
        Node a = graph.getNode("a");
        Node c = graph.getNode("c");

        System.out.println("BFS: " + graph.GraphSearch(a, c, Algorithm.BFS));
        System.out.println("DFS: " + graph.GraphSearch(a, c, Algorithm.DFS));

        for (int i = 1; i <= 5; i++) {
            System.out.println("Random walk run " + i + ": " + graph.GraphSearch(a, c, Algorithm.RANDOM_WALK));
        }
    }
}