package lesson_6_reflection_annotations.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Менеджер для управления ресурсами, реализующими {@link AutoCloseable}.
 * Позволяет регистрировать ресурсы и затем закрывать их все в одном месте
 * с обработкой исключений.
 */
public class ResourceManager implements AutoCloseable {
    private final List<AutoCloseable> resources = new ArrayList<>();
    private static final Logger log = LogManager.getLogger(ResourceManager.class);

    /**
     * Регистрирует ресурс для последующего закрытия.
     *
     * @param resource ресурс (не null)
     */
    public void register(AutoCloseable resource) {
        if (resource != null) {
            resources.add(resource);
        }
    }

    /**
     * Закрывает все зарегистрированные ресурсы в порядке регистрации.
     * Любые исключения при закрытии логируются, но не прерывают процесс.
     */
    @Override
    public void close() {
        for (AutoCloseable r : resources) {
            try {
                r.close();
            } catch (Exception e) {
                log.warn("Ошибка при закрытии ресурса {}: {}",
                        r.getClass().getSimpleName(), e.getMessage());
            }
        }
    }
}
