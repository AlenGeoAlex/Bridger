package io.github.alenalex.bridger.utils;

import com.google.common.base.Objects;

public class Pair <K, V> {

    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K key() {
        return key;
    }

    public V value() {
        return value;
    }

    public boolean isKeyNull() {
        return key == null;
    }

    public boolean isValueNull() {
        return value == null;
    }

    public boolean isNull() {
        return isKeyNull() && isValueNull();
    }

    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equal(key, pair.key) && Objects.equal(value, pair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key, value);
    }
}
