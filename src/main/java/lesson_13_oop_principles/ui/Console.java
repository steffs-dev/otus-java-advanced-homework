package lesson_13_oop_principles.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Консольная реализация {@link UI}.
 * <p>Использует {@link System#out} для вывода и {@link BufferedReader}
 * поверх {@link System#in} для ввода.</p>
 */

public class Console implements UI {

    private final BufferedReader reader;

    public Console() {
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public void printf(String message, Object... args) {
        System.out.printf(message, args);
    }

    @Override
    public String readLine(){
        try{
            return reader.readLine();
        }catch(IOException e){
            throw new RuntimeException("Error reading input", e);
        }
    }

    @Override
    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error while closing resources", e);
        }
    }
}
