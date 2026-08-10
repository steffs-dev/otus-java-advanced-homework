package lesson_11_bytecode_classloader;

public class Application {
    public static void main(String[] args) {
        TestLoggingInterface test = TestLogger.createLogger();
        test.calculation(1);
        test.calculation(2, 2);
        test.calculation(2, 3, "Дай ");
    }
}
