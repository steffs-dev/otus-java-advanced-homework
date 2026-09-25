package lesson_23_cache.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) {
        new HWCacheDemo().demo();
    }

    private void demo() {
        HwCache<String, Integer> cache = new MyCache<>();

        // пример, когда Idea предлагает упростить код, при этом может появиться "спец"-эффект
        @SuppressWarnings("java:S1604")
        HwListener<String, Integer> listener = new HwListener<String, Integer>() {
            @Override
            public void notify(String key, Integer value, String action) {
                logger.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        cache.addListener(listener);
        String key = "key: 0";
        cache.put("key: 0".intern(), 555);
        for (int i = 1; i < 50; i++) {
            cache.put("key: " + i, i);
        }
        logger.info("before gc: {}", cache.getAll().size());

        System.gc();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("after gc: {}", cache.getAll().size());

        logger.info("getValue:{}", cache.get("key: 0"));

//        cache.remove("key: 1");
        cache.removeListener(listener);
    }
}
