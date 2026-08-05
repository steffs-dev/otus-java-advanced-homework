package lesson_11_bytecode_classloader;

public class TestLogging implements TestLoggingInterface {

    @Log
    @Override
    public void calculation(int param) {
        int result = param;
        System.out.println("result = " + result);
    }

    @Override
    public void calculation(int param1, int param2) {
        int result = param1 + param2;
        System.out.println("result = " + result);
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        String result = param3 + (param1 + param2);
        System.out.println("result = " + result);
    }

}
