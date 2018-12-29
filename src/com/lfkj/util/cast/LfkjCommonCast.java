package com.lfkj.util.cast;

import com.lfkj.util.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class LfkjCommonCast {

    @SafeVarargs
    public static <MapType extends Map<K, V>, K, V> MapType mapOf(Supplier<MapType> constructor, Pair<K, V>... entries) {
        MapType map = constructor.get();
        for (Pair<K, V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

    @SafeVarargs
    public static <K, V> HashMap<K, V> mapOf(Pair<K, V>... entries) {
        return mapOf(HashMap::new, entries);
    }

    public static <K, V> Pair<K, V> pairOf(K key, V value) {
        return Pair.of(key, value);
    }
}
