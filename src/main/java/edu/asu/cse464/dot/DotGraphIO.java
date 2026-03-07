package edu.asu.cse464.dot;

import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static guru.nidi.graphviz.model.Factory.mutGraph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public final class DotGraphIO {
  private DotGraphIO() {}

  public static DirectedGraph parseGraph(String filepath) throws IOException {
    String dot = Files.readString(Path.of(filepath), StandardCharsets.UTF_8);
    return DotParser.parse(dot);
  }

  public static void outputGraph(String filepath, DirectedGraph graph) throws IOException {
    Path out = Path.of(filepath);
    Path parent = out.toAbsolutePath().getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }
    Files.writeString(out, graph.toString(), StandardCharsets.UTF_8);
  }

  public static void outputDOTGraph(String path, DirectedGraph graph) throws IOException {
    Path out = Path.of(path);
    Path parent = out.toAbsolutePath().getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }
    Files.writeString(out, graph.toDot(), StandardCharsets.UTF_8);
  }

  public static void outputGraphics(String path, String format, DirectedGraph graph) throws IOException {
    if (format == null || format.isBlank()) {
      throw new IllegalArgumentException("format must be non-empty (e.g., png).");
    }

    Format f;
switch (format.toLowerCase()) {
  case "png":
    f = Format.PNG;
    break;
  case "svg":
    f = Format.SVG;
    break;
  default:
    throw new IllegalArgumentException("Unsupported format: " + format + " (support: png, svg)");
}

    Path out = Path.of(path);
    Path parent = out.toAbsolutePath().getParent();
    if (parent != null) {
      Files.createDirectories(parent);
    }

    Graphviz.fromGraph(toMutableGraph(graph)).render(f).toFile(out.toFile());
  }

  private static MutableGraph toMutableGraph(DirectedGraph graph) {
    MutableGraph g = mutGraph("G").setDirected(true);

    Map<String, MutableNode> nodes = new HashMap<>();
    for (String label : graph.getNodes()) {
      MutableNode n = mutNode(label).add(Label.of(label));
      nodes.put(label, n);
      g.add(n);
    }

    for (DirectedEdge e : graph.getEdges()) {
      MutableNode src = nodes.computeIfAbsent(e.src(), k -> mutNode(k).add(Label.of(k)));
      MutableNode dst = nodes.computeIfAbsent(e.dst(), k -> mutNode(k).add(Label.of(k)));
      src.addLink(dst);
      g.add(src);
      g.add(dst);
    }

    return g;
  }
}