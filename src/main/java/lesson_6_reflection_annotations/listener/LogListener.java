package lesson_6_reflection_annotations.listener;

import lesson_6_reflection_annotations.events.Event;
import lesson_6_reflection_annotations.events.ServiceEvent;
import lesson_6_reflection_annotations.events.StatisticsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;


/**
 * Слушатель, записывающий все события тестирования в текстовый файл (log.txt).
 * При создании проверяет наличие директории и файла, создаёт при необходимости.
 * Использует {@link FileChannel} для записи в режиме APPEND.
 * Реализует {@link AutoCloseable} для корректного закрытия канала.
 */
public class LogListener implements TestListener, AutoCloseable {
    private final FileChannel fileChannel;

    private static final Logger log = LogManager.getLogger(LogListener.class);

    /**
     * Конструктор, создающий директорию и файл по указанному пути.
     *
     * @param dir путь к директории (без имени файла)
     * @throws RuntimeException если не удалось создать директорию или файл
     */
    public LogListener(String dir) {
        try {
            Path filepath = createDirAndFileIfNotExists(dir).toPath();
            fileChannel = FileChannel.open(filepath, StandardOpenOption.APPEND);
        } catch (IOException e) {
            log.error("Ошибка при создании файла для записи результатов тестирования {}",
                    e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private File createDirAndFileIfNotExists(String dir) throws IOException {
        if (dir == null) {
            log.error("Ошибка при задании пути к файлу");
            throw new RuntimeException("Ошибка при задании пути к файлу");
        }
        File dirFile = new File(dir);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        return createFile(dir);
    }

    private File createFile(String dir) throws IOException {
        File file = new File(dir + "/log.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }

    /**
     * Записывает строку в файл в кодировке UTF-8.
     *
     * @param msg текст для записи
     */
    private void writeLog(String msg) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
        try {
            fileChannel.write(byteBuffer);
        } catch (IOException e) {
            log.warn("Ошибка при записи результатов тестирования в файл {}", e.getMessage());
        }
    }

    @Override
    public void beforeTests() {
        String msg = "-------ЗАПУСК ТЕСТИРОВАНИЯ-------\n";
        writeLog(msg);
    }


    @Override
    public void onEmptyTestsList(ServiceEvent event) {
        String msg = String.format("В классе %s нет методов, помеченных аннотацией @Test\n",
                parseDescription(event));
        writeLog(msg);
    }

    @Override
    public void onTestStart(ServiceEvent event) {
        String msg = String.format("Начало выполнения теста: %s\n", parseDescription(event));
        writeLog(msg);
    }

    @Override
    public void onTestSuccess(ServiceEvent event) {
        String msg = String.format("Тест %s пройден за %d мс\n",
                parseDescription(event), event.getDuration().toMillis());
        writeLog(msg);
    }

    @Override
    public void onTestFailure(ServiceEvent event) {
        String msg = String.format("Тест %s провален. Причина: %s\n",
                parseDescription(event), Arrays.toString(event.getThrowable().getStackTrace()));
        writeLog(msg);
    }

    @Override
    public void afterTests(StatisticsEvent event) {
        String msg = String.format("""
                        -------ТЕСТИРОВАНИЕ ЗАВЕРШЕНО-------
                        Статистика выполнения тестов (класс: %s):
                        Общее количество тестов: %d, из них:
                        - тестов пройдено: %d;
                        - тестов провалено: %d.
                        
                        """,
                parseDescription(event),
                event.getTotalTests(),
                event.getPassedTests(), event.getFailedTests()
        );
        writeLog(msg);
    }

    private String parseDescription(Event event) {
        return (event.getDescription() == null) ? event.getName()
                : event.getName() + " (" + event.getDescription() + ")";
    }

    @Override
    public void close() throws Exception {
        fileChannel.close();
    }
}
