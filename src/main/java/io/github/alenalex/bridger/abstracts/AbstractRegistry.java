package io.github.alenalex.bridger.abstracts;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.exceptions.IllegalRegistryOperation;
import io.github.alenalex.bridger.utils.Pair;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class AbstractRegistry<K, V> {

    protected final Bridger plugin;
    private final HashMap<K, V> registry;

    private K defaultKey;

    public AbstractRegistry(Bridger plugin) {
        this.plugin = plugin;
        this.registry = new HashMap<>();
        this.defaultKey = null;
    }

    public void register(K key, V value) {
        if(isKeyRegistered(key)) {
            throw new IllegalRegistryOperation("Key " + key + " is already registered!. use registerOverride() instead.");
        }
        registry.put(key, value);
    }

    public void register(Pair<K, V> pair) {
        register(pair.key(), pair.value());
    }

    public Optional<Pair<K, V>> registerOverride(Pair<K, V> pair) {
        return registerOverride(pair.key(), pair.value());
    }

    public Optional<Pair<K, V>> registerOverride(K key, V value) {
        Pair<K, V> pair = null;
        if(isKeyRegistered(key)) {
             pair = new Pair<>(key, get(key));
        }

        registry.put(key, value);
        return Optional.ofNullable(pair);
    }

    public void update(K key, V value) {
        if(!isKeyRegistered(key)) {
            throw new IllegalRegistryOperation("Key " + key + " is not registered!");
        }
        registry.replace(key, value);
    }

    public V get(K key) {
        return registry.get(key);
    }

    public V getOrDefault(K key) {
        if(!isDefaultKeySet())
            throw new IllegalRegistryOperation("Default key is not set!");

        return getOrDefaultKey(key, defaultKey);
    }

    public V getOrDefault(K key, V defaultValue) {
        return registry.getOrDefault(key, defaultValue);
    }

    public V getOrDefaultKey(K key, K defaultKey) {
        return registry.getOrDefault(key, get(defaultKey));
    }

    public boolean isKeyRegistered(K key) {
        return registry.containsKey(key);
    }

    public boolean isValueRegistered(V value) {
        return registry.containsValue(value);
    }

    public K getDefaultKey() {
        return defaultKey;
    }

    public void setDefaultKey(K key) {
        this.defaultKey = key;
    }

    public boolean isDefaultKeySet() {
        return defaultKey != null;
    }

    public boolean isEmpty() {
        return registry.isEmpty();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public int sizeOf() {
        return registry.size();
    }

    public void removeAll() {
        registry.clear();
    }

    public void remove(K key) {
        registry.remove(key);
    }

    public void removeByValue(V value) {
        registry.entrySet().removeIf(entry -> entry.getValue().equals(value));
    }

    public Iterator<K> getKeyIterator() {
        return registry.keySet().iterator();
    }

    public Iterator<V> getValueIterator() {
        return registry.values().iterator();
    }

    public List<K> getModifiableKeyList() {
        return new ArrayList<>(registry.keySet());
    }

    public List<V> getModifiableValueList() {
        return new ArrayList<>(registry.values());
    }

    public Set<Map.Entry<K, V>> getEntrySet() {
        return registry.entrySet();
    }

    public Pair<K, V> getPair(K key) {
        return new Pair<>(key, get(key));
    }

    public boolean runTask(Consumer<Pair<K, V>> task) {
        registry.keySet().forEach(key -> task.accept(getPair(key)));
        return true;
    }

    public V onDefault(){
        if(defaultKey == null)
            throw new IllegalRegistryOperation("Default key is not set!");

        return get(defaultKey);
    }

    public CompletableFuture<Boolean> runTaskAsync(Consumer<Pair<K, V>> task) {
        return CompletableFuture.supplyAsync(() -> runTask(task));
    }
}
