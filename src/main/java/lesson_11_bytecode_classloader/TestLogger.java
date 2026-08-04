package lesson_11_bytecode_classloader;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class TestLogger {

    public static TestLoggingInterface createLogger() {
        InvocationHandler handler = new LoggingHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(
                TestLogger.class.getClassLoader(),
                new Class[]{TestLoggingInterface.class},
                handler
        );
    }

    private record LoggingHandler(TestLoggingInterface logger) implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isLoggable(method)) {
                method.setAccessible(true);
                StringBuilder sb = new StringBuilder("executed method: " + method.getName());
                for (Object param : args) {
                    sb.append(", param: ").append(param.toString());
                }
                System.out.println(sb);
                return method.invoke(logger, args);
            }

            return method.invoke(logger, args);
        }

        private boolean isLoggable(Method method) {
            return method.isAnnotationPresent(Log.class);
        }
    }
}
