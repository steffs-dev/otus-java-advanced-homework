package lesson_16_structural_patterns.homework.src.main.java.ru.otus.processor;

import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessorConcatFieldsTest {
    @Test
    @DisplayName("Процессор должен поменять местами field11 и field12")
    void shouldSwapField11AndField12() {
        //given
        Message msg = new Message.Builder(1L)
                .field11("val11")
                .field12("val12")
                .build();

        Processor processor = new ProcessorSwapField11AndField12();

        //when
        Message result = processor.process(msg);

        //then
        assertThat(result.getField11()).isEqualTo("val12");
        assertThat(result.getField12()).isEqualTo("val11");

    }

}