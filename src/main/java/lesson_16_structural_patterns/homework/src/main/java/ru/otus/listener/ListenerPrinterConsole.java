package lesson_16_structural_patterns.homework.src.main.java.ru.otus.listener;

import lesson_16_structural_patterns.homework.src.main.java.ru.otus.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ListenerPrinterConsole implements Listener {
    private static final Logger logger = LoggerFactory.getLogger(ListenerPrinterConsole.class);

    @Override
    public void onUpdated(Message msg) {
        logger.info("oldMsg:{}", msg);
    }
}
