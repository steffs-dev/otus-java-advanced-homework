package lesson_23_cache.cachehw;

import java.util.Map;

public interface HwCache<K, V> {

    void put(K key, V value);

    void remove(K key);

    V get(K key);

    void addListener(HwListener<K, V> listener);

    void removeListener(HwListener<K, V> listener);

    Map<K, V> getAll();
}
