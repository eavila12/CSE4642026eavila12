
package edu.asu.cse464.dot;

import java.util.ArrayDeque;
import java.util.Queue;

final class QueueFrontier<T> implements Frontier<T> {
    private final Queue<T> queue = new ArrayDeque<>();

    @Override
    public void add(T value) {
        queue.add(value);
    }

    @Override
    public T remove() {
        return queue.remove();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}