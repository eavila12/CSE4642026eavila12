package edu.asu.cse464.dot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

final class RandomFrontier<T> implements Frontier<T> {
    private final List<T> values = new ArrayList<>();
    private final Random random;

    RandomFrontier() {
        this(new Random());
    }

    RandomFrontier(Random random) {
        this.random = random;
    }

    @Override
    public void add(T value) {
        values.add(value);
    }

    @Override
    public T remove() {
        int index = random.nextInt(values.size());
        return values.remove(index);
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty();
    }
}