package edu.asu.cse464.dot;

interface Frontier<T> {
    void add(T value);
    T remove();
    boolean isEmpty();
}