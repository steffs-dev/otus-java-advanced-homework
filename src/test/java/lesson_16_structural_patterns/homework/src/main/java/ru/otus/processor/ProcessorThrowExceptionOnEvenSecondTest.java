package lesson_16_structural_patterns.homework.src.main.java.ru.otus.processor;

import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;


class ProcessorThrowExceptionOnEvenSecondTest {

    private static final Message msg = new Message.Builder(1L).build();

    @Test
    @DisplayName("Процессор должен бросать исключение в четную секунду")
    void shouldThrowExceptionOnEvenSecond() {
        //given
        Processor processor = new ProcessorThrowExceptionOnEvenSecond(
                () -> LocalDateTime.of
                        (2026, 9, 13, 10, 0, 0)
        );

        //when /then
        assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> processor.process(msg))
                .withMessageContaining("Even second");
    }

    @Test
    @DisplayName("Процессор не должен бросать исключение в нечетную секунду")
    void shouldNotThrowExceptionOnOddSecond() {
        //given
        Processor processor = new ProcessorThrowExceptionOnEvenSecond(
                () -> LocalDateTime.of
                        (2026, 9, 13, 10, 0, 1)
        );

        //when
        Message result = processor.process(msg);

        //then
        assertThat(result).isSameAs(msg);
    }
}

