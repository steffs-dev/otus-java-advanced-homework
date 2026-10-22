package lesson_26_di_ioc;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import lesson_26_di_ioc.appcontainer.AppComponentsContainerImpl;
import lesson_26_di_ioc.appcontainer.api.AppComponent;
import lesson_26_di_ioc.appcontainer.api.AppComponentsContainerConfig;
import lesson_26_di_ioc.config.AppConfig;
import lesson_26_di_ioc.services.*;

import java.io.PrintStream;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

class AppTest {

//    @Disabled("Эту аннотацию надо убрать")
    @DisplayName("Из контекста тремя способами должен корректно доставаться компонент с проставленными полями")
    @ParameterizedTest(name = "Достаем по: {0}")
    @CsvSource(
            value = {
                "GameProcessor, lesson_26_di_ioc.services.GameProcessor",
                "GameProcessorImpl, lesson_26_di_ioc.services.GameProcessor",
                "gameProcessor, lesson_26_di_ioc.services.GameProcessor",
                "IOService, lesson_26_di_ioc.services.IOService",
                "IOServiceStreams, lesson_26_di_ioc.services.IOService",
                "ioService, lesson_26_di_ioc.services.IOService",
                "PlayerService, lesson_26_di_ioc.services.PlayerService",
                "PlayerServiceImpl, lesson_26_di_ioc.services.PlayerService",
                "playerService, lesson_26_di_ioc.services.PlayerService",
                "EquationPreparer, lesson_26_di_ioc.services.EquationPreparer",
                "EquationPreparerImpl, lesson_26_di_ioc.services.EquationPreparer",
                "equationPreparer, lesson_26_di_ioc.services.EquationPreparer"
            })
    void shouldExtractFromContextCorrectComponentWithNotNullFields(String classNameOrBeanId, Class<?> rootClass)
            throws Exception {
        var ctx = new AppComponentsContainerImpl(AppConfig.class);

        assertThat(classNameOrBeanId).isNotEmpty();
        Object component;
        if (classNameOrBeanId.charAt(0) == classNameOrBeanId.toUpperCase().charAt(0)) {
            Class<?> gameProcessorClass = Class.forName("lesson_26_di_ioc.services." + classNameOrBeanId);
            assertThat(rootClass).isAssignableFrom(gameProcessorClass);

            component = ctx.getAppComponent(gameProcessorClass);
        } else {
            component = ctx.getAppComponent(classNameOrBeanId);
        }
        assertThat(component).isNotNull();
        assertThat(rootClass).isAssignableFrom(component.getClass());

        var fields = Arrays.stream(component.getClass().getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .peek(f -> f.setAccessible(true))
                .toList();

        for (var field : fields) {
            var fieldValue = field.get(component);
            assertThat(fieldValue)
                    .isNotNull()
                    .isInstanceOfAny(
                            IOService.class,
                            PlayerService.class,
                            EquationPreparer.class,
                            PrintStream.class,
                            Scanner.class);
        }
    }

//    @Disabled("Эту аннотацию надо убрать")
    @DisplayName("В контексте не должно быть компонентов с одинаковым именем")
    @Test
    void shouldNotAllowTwoComponentsWithSameName() {
        assertThatCode(() -> new AppComponentsContainerImpl(ConfigWithTwoComponentsWithSameName.class))
                .isInstanceOf(Exception.class);
    }

//    @Disabled("Эту аннотацию надо убрать")
    @DisplayName(
            "При попытке достать из контекста отсутствующий или дублирующийся компонент, должно выкидываться исключение")
    @Test
    void shouldThrowExceptionWhenContainerContainsMoreThanOneOrNoneExpectedComponents() {
        var ctx = new AppComponentsContainerImpl(ConfigWithTwoSameComponents.class);

        assertThatCode(() -> ctx.getAppComponent(EquationPreparer.class)).isInstanceOf(Exception.class);

        assertThatCode(() -> ctx.getAppComponent(PlayerService.class)).isInstanceOf(Exception.class);

        assertThatCode(() -> ctx.getAppComponent("equationPreparer3")).isInstanceOf(Exception.class);
    }

    @AppComponentsContainerConfig(order = 1)
    public static class ConfigWithTwoComponentsWithSameName {

        @AppComponent(order = 1, name = "equationPreparer")
        public EquationPreparer equationPreparer1() {
            return new EquationPreparerImpl();
        }

        @AppComponent(order = 1, name = "equationPreparer")
        public IOService ioService() {
            return new IOServiceStreams(System.out, System.in);
        }
    }

    @AppComponentsContainerConfig(order = 1)
    public static class ConfigWithTwoSameComponents {

        @AppComponent(order = 1, name = "equationPreparer1")
        public EquationPreparer equationPreparer1() {
            return new EquationPreparerImpl();
        }

        @AppComponent(order = 1, name = "equationPreparer2")
        public EquationPreparer equationPreparer2() {
            return new EquationPreparerImpl();
        }
    }
}
