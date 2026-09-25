package lesson_23_cache.cachehw;

import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы
    List<HwListener<K, V>> listeners = new ArrayList<>();
    Map<K, V> cacheMap = new WeakHashMap<>();

    @Override
    public void put(K key, V value) {
        try {
            cacheMap.put(key, value);
            listeners.forEach(l -> l.notify(key, value, "putToCache"));
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void remove(K key) {
        try {
            V v = cacheMap.get(key);
            cacheMap.remove(key);
            listeners.forEach(l -> l.notify(key, v, "removeFromCache"));
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public V get(K key) {
        try {
            V v = cacheMap.get(key);
            listeners.forEach(l -> l.notify(key, v, "getFromCache"));
            return v;
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        try {
            listeners.add(listener);
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        try {
            listeners.remove(listener);
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Map<K, V> getAll() {
        try {
            return new HashMap<>(cacheMap);
        } catch (Exception e) {
            throw new UnsupportedOperationException();
        }
    }
}
