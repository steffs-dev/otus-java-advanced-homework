package lesson_17_serialization.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Утилитарный класс для работы с Jackson {@link ObjectMapper}.
 *
 * <p>Содержит общий настроенный экземпляр сериализатора/десериализатора,
 * чтобы не создавать {@link ObjectMapper} многократно.</p>
 */

public class JsonUtil {

    /**
     * Общий экземпляр {@link ObjectMapper}.
     *
     * <p>Настройки:</p>
     * <ul>
     *     <li>не падать при неизвестных свойствах;</li>
     *     <li>включить красивый вывод JSON;</li>
     *     <li>поддержать работу с типами даты/времени из Java Time API.</li>
     * </ul>
     */

    public static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        MAPPER.registerModule(new JavaTimeModule());
    }

    public JsonUtil() {
    }

    /**
     * Возвращает общий настроенный {@link ObjectMapper}.
     *
     * @return объект {@link ObjectMapper}
     */

    public static ObjectMapper getMAPPER() {
        return MAPPER;
    }
}
