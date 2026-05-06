package edu.asu.cse464.dot;

import java.nio.file.Files;
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

        Files.createDirectories(outDir);

        DirectedGraph graph = DotGraphIO.parseGraph(input.toString());

        System.out.println(graph);
        DotGraphIO.outputGraph(outDir.resolve("graph.txt").toString(), graph);
        DotGraphIO.outputDOTGraph(outDir.resolve("graph.dot").toString(), graph);
        DotGraphIO.outputGraphics(outDir.resolve("graph.png").toString(), "png", graph);

        Node src = graph.getNode("a");
        Node dst = graph.getNode("h");

        System.out.println("BFS a -> h: " + graph.GraphSearch(src, dst, Algorithm.BFS));
        System.out.println("DFS a -> h: " + graph.GraphSearch(src, dst, Algorithm.DFS));

        for (int i = 1; i <= 5; i++) {
            System.out.println(
                "Random walk a -> h run " + i + ": "
                    + graph.GraphSearch(src, dst, Algorithm.RANDOM_WALK)
            );
        }
    }
}