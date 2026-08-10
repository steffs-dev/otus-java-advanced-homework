package lesson_11_bytecode_classloader;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.*;

class TestLoggerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        outContent.reset();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void createLoggerShouldReturnProxyInstance() {
        TestLoggingInterface logger = TestLogger.createLogger();
        assertNotNull(logger);
        assertTrue(logger instanceof TestLoggingInterface);
        assertTrue(Proxy.isProxyClass(logger.getClass()));
    }

    @Test
    void annotatedMethodWithSingleParamShouldBeLogged() {
        TestLoggingInterface logger = TestLogger.createLogger();
        logger.calculation(5);

        String output = outContent.toString();
        assertTrue(output.contains("executed method: calculation, param: 5"));
        assertTrue(output.contains("result = 5"));
    }

    @Test
    void annotatedMethodWithMultipleParamsShouldBeLogged() {
        TestLoggingInterface logger = TestLogger.createLogger();
        logger.calculation(2, 3, "Дай ");

        String output = outContent.toString();
        assertTrue(output.contains("executed method: calculation, param: 2, param: 3, param: Дай "));
        assertTrue(output.contains("result = Дай 5"));
    }

    @Test
    void nonAnnotatedMethodShouldNotBeLogged() {
        TestLoggingInterface logger = TestLogger.createLogger();
        logger.calculation(2, 2);

        String output = outContent.toString();
        assertFalse(output.contains("executed method: calculation"));
        assertTrue(output.contains("result = 4"));
    }

    @Test
    void onlyAnnotatedMethodsShouldProduceLogs() {
        TestLoggingInterface logger = TestLogger.createLogger();
        logger.calculation(1);
        logger.calculation(2, 2);
        logger.calculation(3, 4, "Hello ");

        String output = outContent.toString();

        long logCount = output.lines()
                .filter(line -> line.contains("executed method"))
                .count();
        assertEquals(2, logCount, "Only two methods are annotated with @Log");

        assertTrue(output.contains("result = 1"));
        assertTrue(output.contains("result = 4"));
        assertTrue(output.contains("result = Hello 7"));
    }

}