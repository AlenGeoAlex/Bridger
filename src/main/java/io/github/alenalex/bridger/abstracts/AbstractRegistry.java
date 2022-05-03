package io.github.alenalex.bridger.abstracts;

import io.github.alenalex.bridger.Bridger;
import io.github.alenalex.bridger.exceptions.IllegalRegistryOperation;
import io.github.alenalex.bridger.utils.Pair;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Stream;

public abstract class AbstractRegistry<K, V> {

    protected final Bridger plugin;
    private final ConcurrentHashMap<K, V> registry;

    private K defaultKey;

    public AbstractRegistry(Bridger plugin) {
        this.plugin = plugin;
        this.registry = new ConcurrentHashMap<>();
        this.defaultKey = null;
    }

    public void register(K key, V value) {
        if(isKeyRegistered(key)) {
            throw new IllegalRegistryOperation("Key " + key +"#"+key.getClass().getSimpleName()+ " is already registered with value#"+value.getClass().getSimpleName()+"!. use registerOverride() instead.");
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
             pair = new Pair<>(key, of(key));
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

    public V of(K key) {
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
        return registry.getOrDefault(key, of(defaultKey));
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

    public Pair<K, V> pop(K key){
        if(registry.containsKey(key)){
            Pair<K, V> kvPair = new Pair<>(key, of(key));
            registry.remove(key);
            return kvPair;
        }else return null;
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
        return new Pair<>(key, of(key));
    }

    public boolean runTask(Consumer<Pair<K, V>> task) {
        registry.keySet().forEach(key -> task.accept(getPair(key)));
        return true;
    }

    public V onDefault(){
        if(defaultKey == null)
            throw new IllegalRegistryOperation("Default key is not set!");

        return of(defaultKey);
    }

    public CompletableFuture<Boolean> runTaskAsync(Consumer<Pair<K, V>> task) {
        return CompletableFuture.supplyAsync(() -> runTask(task));
    }

    public Stream<V> getValueStream(){
        return registry.values().stream();
    }

    public Stream<K> getKeyStream(){
        return registry.keySet().stream();
    }

    public Collection<V> getValueCollection(){
        return registry.values();
    }

    public Collection<K> getKeyCollection(){
        return registry.keySet();
    }


}
