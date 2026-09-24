package lesson_23_cache.cachehw;

public interface HwListener<K, V> {
    void notify(K key, V value, String action);
}
