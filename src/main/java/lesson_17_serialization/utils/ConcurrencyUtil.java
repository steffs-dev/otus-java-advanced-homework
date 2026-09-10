package lesson_17_serialization.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Утилитарный класс для работы с многопоточностью.
 *
 * <p>Предоставляет вспомогательные методы для управления жизненным циклом пулов потоков,
 * в частности для их безопасного и корректного завершения (graceful shutdown).</p>
 *
 * <p>Класс содержит только статические методы и не предполагает создание экземпляров.</p>
 */

public class ConcurrencyUtil {

    /**
     * Логгер для фиксации событий, связанных с остановкой и прерыванием задач в пулах потоков.
     */

    private static final Logger log = LoggerFactory.getLogger(ConcurrencyUtil.class);

    /**
     * Выполняет безопасное завершение работы одного или нескольких {@link ExecutorService}.
     *
     * <p>Алгоритм работы для каждого переданного исполнителя:</p>
     * <ol>
     *     <li>Инициируется упорядоченное завершение ({@link ExecutorService#shutdown()}),
     *         при котором ранее отправленные задачи продолжают выполняться, но новые не принимаются.</li>
     *     <li>Метод ожидает завершения выполнения задач в течение 3 секунд.</li>
     *     <li>Если за отведенное время пул не завершился, выполняется принудительное завершение
     *         ({@link ExecutorService#shutdownNow()}), которое прерывает активные задачи и возвращает список
     *         задач, которые так и не успели начаться.</li>
     *     <li>Если поток, вызвавший данный метод, был прерван во время ожидания,
     *         также выполняется принудительное завершение, а статус прерывания текущего потока восстанавливается.</li>
     * </ol>
     *
     * @param executors один или несколько пулов потоков (varargs), которые необходимо остановить
     */

    public static void shutdown(ExecutorService... executors) {
        for (ExecutorService executor : executors) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                    List<Runnable> tasksInTry = executor.shutdownNow();
                    log.info("{} tasks were interrupted in try", tasksInTry.size());
                }
            } catch (InterruptedException e) {
                List<Runnable> tasksInCatch = executor.shutdownNow();
                log.info("{} tasks were interrupted in catch", tasksInCatch.size());
                Thread.currentThread().interrupt();
            }
        }
    }
}
