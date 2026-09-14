package lesson_17_serialization;

/**
 * Главный класс приложения.
 *
 * <p>Содержит точку входа {@link #main(String[])}.
 * Задает рабочие директории и запускает обработку запросов.</p>
 */

public class LibApplication {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы командной строки
     */

    public static void main(String[] args) {

        /**
         * Директория, в которой приложение ищет входящие JSON-запросы.
         */

        String dirToScan = "src/main/java/lesson_17_serialization/dirToCheck/";

        /**
         * Директория, в которую приложение сохраняет JSON-ответы.
         */

        String dirToResp = "src/main/java/lesson_17_serialization/dirToResp/";

        LibraryFacade lib = new LibraryFacade();
        lib.serveVisitor(dirToScan, dirToResp);
    }
}
