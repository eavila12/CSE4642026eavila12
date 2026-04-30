package edu.asu.cse464.dot;

import java.util.ArrayDeque;
import java.util.Deque;

final class StackFrontier<T> implements Frontier<T> {
    private final Deque<T> stack = new ArrayDeque<>();

    @Override
    public void add(T value) {
        stack.push(value);
    }

    @Override
    public T remove() {
        return stack.pop();
    }

    @Override
    public boolean isEmpty() {
        return stack.isEmpty();
    }
}