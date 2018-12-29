package com.lfkj.util;

public class Pair<K, V> extends javafx.util.Pair<K, V> {

    public final K _1 = getKey();
    public final V _2 = getValue();

    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    private Pair(K key, V value) {
        super(key, value);
    }

    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    public K key() {
        return _1;
    }

    public V value() {
        return _2;
    }
}
