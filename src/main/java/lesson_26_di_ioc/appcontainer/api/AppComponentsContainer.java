package lesson_26_di_ioc.appcontainer.api;

public interface AppComponentsContainer {
    <C> C getAppComponent(Class<C> componentClass);

    <C> C getAppComponent(String componentName);
}
