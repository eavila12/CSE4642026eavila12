package edu.asu.cse464.dot;

import java.nio.file.Path;

public final class App {
  private App() {}

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.out.println("Usage: java ... edu.asu.cse464.dot.App <input.dot> <outDir>");
      System.exit(2);
    }

    Path input = Path.of(args[0]);
    Path outDir = Path.of(args[1]);

    DirectedGraph g = DotGraphIO.parseGraph(input.toString());

    System.out.println(g);

    DotGraphIO.outputGraph(outDir.resolve("graph.txt").toString(), g);
    DotGraphIO.outputDOTGraph(outDir.resolve("graph.dot").toString(), g);
    DotGraphIO.outputGraphics(outDir.resolve("graph.png").toString(), "png", g);
  }
}