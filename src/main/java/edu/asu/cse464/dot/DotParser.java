package edu.asu.cse464.dot;

import java.util.ArrayList;
import java.util.List;

final class DotParser {
  private DotParser() {}

  static DirectedGraph parse(String dot) {
    if (dot == null) {
      throw new IllegalArgumentException("dot must not be null");
    }

    String cleaned = stripComments(dot);
    TokenStream ts = new TokenStream(cleaned);

    ts.consumeOptional("strict");

    if (ts.consumeOptional("digraph")) {
      // ok
    } else if (ts.consumeOptional("graph")) {
      throw new IllegalArgumentException("Only digraph is supported.");
    } else {
      throw new IllegalArgumentException("Expected 'digraph'.");
    }

    ts.consumeOptionalIdOrString(); // optional graph name
    ts.expect("{");

    DirectedGraph g = new DirectedGraph();

    while (!ts.peek("}")) {
      if (ts.peek(";")) {
        ts.next();
        continue;
      }

      String left = ts.consumeIdOrString();
      if (ts.consumeOptional("->")) {
        List<String> chain = new ArrayList<>();
        chain.add(left);
        chain.add(ts.consumeIdOrString());
        while (ts.consumeOptional("->")) {
          chain.add(ts.consumeIdOrString());
        }
        for (int i = 0; i < chain.size() - 1; i++) {
          g.addEdge(chain.get(i), chain.get(i + 1));
        }
        skipAttributes(ts);
        ts.consumeOptional(";");
      } else {
        g.addNode(left);
        skipAttributes(ts);
        ts.consumeOptional(";");
      }
    }

    ts.expect("}");
    return g;
  }

  private static void skipAttributes(TokenStream ts) {
    if (!ts.peek("[")) return;

    int depth = 0;
    while (ts.hasNext()) {
      String t = ts.next();
      if ("[".equals(t)) depth++;
      else if ("]".equals(t)) {
        depth--;
        if (depth == 0) return;
      }
    }
    throw new IllegalArgumentException("Unclosed attribute list [].");
  }

  private static String stripComments(String s) {
    StringBuilder out = new StringBuilder();
    int i = 0;
    while (i < s.length()) {
      char c = s.charAt(i);

      if (c == '/' && i + 1 < s.length() && s.charAt(i + 1) == '/') {
        i += 2;
        while (i < s.length() && s.charAt(i) != '\n') i++;
        continue;
      }

      if (c == '/' && i + 1 < s.length() && s.charAt(i + 1) == '*') {
        i += 2;
        while (i + 1 < s.length() && !(s.charAt(i) == '*' && s.charAt(i + 1) == '/')) i++;
        i = Math.min(i + 2, s.length());
        continue;
      }

      if (c == '#') {
        i++;
        while (i < s.length() && s.charAt(i) != '\n') i++;
        continue;
      }

      out.append(c);
      i++;
    }
    return out.toString();
  }

  private static final class TokenStream {
    private final List<String> tokens;
    private int pos = 0;

    TokenStream(String s) {
      this.tokens = tokenize(s);
    }

    boolean hasNext() {
      return pos < tokens.size();
    }

    boolean peek(String t) {
      return hasNext() && tokens.get(pos).equals(t);
    }

    String next() {
      if (!hasNext()) throw new IllegalArgumentException("Unexpected end of input.");
      return tokens.get(pos++);
    }

    void expect(String t) {
      String got = next();
      if (!t.equals(got)) {
        throw new IllegalArgumentException("Expected '" + t + "' but got '" + got + "'");
      }
    }

    boolean consumeOptional(String t) {
      if (peek(t)) {
        pos++;
        return true;
      }
      return false;
    }

    String consumeIdOrString() {
      String t = next();
      if (isId(t) || isQuoted(t)) return unquoteIfNeeded(t);
      throw new IllegalArgumentException("Expected identifier or string, got: " + t);
    }

    void consumeOptionalIdOrString() {
      if (!hasNext()) return;
      String t = tokens.get(pos);
      if (isId(t) || isQuoted(t)) pos++;
    }

    private static boolean isId(String t) {
      return t.matches("[A-Za-z_][A-Za-z0-9_]*");
    }

    private static boolean isQuoted(String t) {
      return t.length() >= 2 && t.startsWith("\"") && t.endsWith("\"");
    }

    private static String unquoteIfNeeded(String t) {
      if (!isQuoted(t)) return t;
      String inner = t.substring(1, t.length() - 1);
      return inner.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    private static List<String> tokenize(String s) {
      List<String> out = new ArrayList<>();
      int i = 0;
      while (i < s.length()) {
        char c = s.charAt(i);

        if (Character.isWhitespace(c)) {
          i++;
          continue;
        }

        if (c == '"') {
          StringBuilder sb = new StringBuilder();
          sb.append(c);
          i++;
          while (i < s.length()) {
            char ch = s.charAt(i);
            sb.append(ch);
            if (ch == '"' && sb.charAt(sb.length() - 2) != '\\') {
              i++;
              break;
            }
            i++;
          }
          out.add(sb.toString());
          continue;
        }

        if (c == '-' && i + 1 < s.length() && s.charAt(i + 1) == '>') {
          out.add("->");
          i += 2;
          continue;
        }

        if ("{}[];=,".indexOf(c) >= 0) {
          out.add(String.valueOf(c));
          i++;
          continue;
        }

        if (Character.isLetter(c) || c == '_') {
          int j = i + 1;
          while (j < s.length()) {
            char ch = s.charAt(j);
            if (Character.isLetterOrDigit(ch) || ch == '_') j++;
            else break;
          }
          out.add(s.substring(i, j));
          i = j;
          continue;
        }

        throw new IllegalArgumentException("Unexpected character in DOT: '" + c + "'");
      }
      return out;
    }
  }
}