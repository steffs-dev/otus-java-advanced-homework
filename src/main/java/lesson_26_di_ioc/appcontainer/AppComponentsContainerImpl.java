package lesson_26_di_ioc.appcontainer;

import lesson_26_di_ioc.appcontainer.api.AppComponent;
import lesson_26_di_ioc.appcontainer.api.AppComponentsContainer;
import lesson_26_di_ioc.appcontainer.api.AppComponentsContainerConfig;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

@FieldDefaults(level = AccessLevel.PRIVATE)
@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        if (initialConfigClasses == null || initialConfigClasses.length == 0) {
            throw new IllegalArgumentException("At least one config class is required");
        }
        initializeConfigClasses(initialConfigClasses);

    }

    public AppComponentsContainerImpl(String configLocation) {
        if(configLocation == null || configLocation.isBlank()) {
            throw new IllegalArgumentException("At least one config location is required");
        }
        Class<?>[] configs;
        try {
            configs = scanConfig(configLocation);
        }catch (IOException e) {
            throw new IllegalStateException("Failed to scan: " + configLocation, e);
        }
        if (configs.length == 0) {
            throw new IllegalArgumentException("No config classes found under: " + configLocation);
        }
            initializeConfigClasses(configs);

    }

    private void initializeConfigClasses(Class<?>... initialConfigClasses) {
        List<Class<?>> initialConfigClassesList = getSortedConfigClassesList(initialConfigClasses);
        for (Class<?> clazz : initialConfigClassesList) {
            processConfig(clazz);
        }
    }

    private void processConfig(Class<?> configClass) {
        Object configInstance = createConfigInstance(configClass);
        List<Method> configComponents = getSortedListByOrderField(configClass);
        registerAppComponentsByName(configInstance, configComponents);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private List<Class<?>> getSortedConfigClassesList(Class<?>... initialConfigClasses) {
        return Arrays.stream(initialConfigClasses)
                .peek(this::checkConfigClass)
                .sorted(Comparator.comparingInt(
                        c -> c.getAnnotation(AppComponentsContainerConfig.class).order()))
                .toList();
    }

    private Object createConfigInstance(Class<?> configClass) {
        try {
            return configClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new IllegalStateException("Cannot instantiate config " + configClass.getName(), e);
        }
    }

    private List<Method> extractMethods(Class<?> classToScan) {
        List<Method> methodsList = new ArrayList<>();
        Method[] methods = classToScan.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(AppComponent.class)) {
                method.setAccessible(true);
                methodsList.add(method);
            }
        }
        return methodsList;
    }

    private List<Method> getSortedListByOrderField(Class<?> classToScan) {
        return extractMethods(classToScan).stream()
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(AppComponent.class).order()))
                .toList();
    }

    private void registerAppComponentsByName(Object configInstance, List<Method> configComponents) {
        for (Method comp : configComponents) {
            String beanName = getBeanName(comp);
            if (appComponentsByName.containsKey(beanName)) {
                throw new IllegalStateException(String.format("Duplicate app component name: %s", beanName));
            }
            appComponentsByName.put(beanName, createBean(configInstance, comp));
        }
    }

    private Object createBean(Object configInstance, Method appComponent) {
        Class<?>[] params = appComponent.getParameterTypes();
        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            args[i] = findBean(params[i]);
        }
        return invokeMethod(configInstance, appComponent, args);
    }

    private Object invokeMethod(Object configInstance, Method appComponent, Object[] args) {
        try {
            return appComponent.invoke(configInstance, args);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String getBeanName(Method appComponent) {
        AppComponent comp = appComponent.getAnnotation(AppComponent.class);
        String name = comp.name();
        if (name.isBlank()) {
            name = appComponent.getName();
        }
        return name;
    }

    private Object findBean(Class<?> type) {
        List<Object> beans = appComponentsByName.values().stream()
                .filter(type::isInstance)
                .toList();
        if (beans.size() > 1) {
            throw new IllegalStateException(String.format("Multiple beans found for type %s", type.getName()));
        }
        if (beans.isEmpty()) {
            throw new NoSuchElementException(String.format("No beans found for type %s", type.getName()));
        }
        return beans.getFirst();
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return componentClass.cast(findBean(componentClass));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        Object bean = appComponentsByName.get(componentName);
        if (bean == null) {
            throw new NoSuchElementException(String.format("No bean found for name %s", componentName));
        }
        return (C) bean;
    }

    private Class<?>[] scanConfig(String configLocation) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String basePath = configLocation.replace('.', '/');
        URL url = classLoader.getResource(basePath);
        if (url == null) {
            throw new IllegalArgumentException("Package not found on classpath: " + configLocation);
        }
        if (!"file".equals(url.getProtocol())) {
            throw new IllegalStateException(
                    "Only file:// URLs supported for scanning, got: " + url);
        }
       Path packageDir;
        try {
            packageDir = Paths.get(url.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Bad classpath URL: " + url, e);
        }
        try(Stream<Path> pathStream = Files.walk(packageDir)) {
            return pathStream
                    .filter(p -> p.toString().endsWith(".class"))
                    .filter(p -> !p.getFileName().toString().equals("module-info.class"))
                    .filter(p -> !p.getFileName().toString().equals("package-info.class"))
                    .map(p -> {
                        String simpleClassName = packageDir.relativize(p).toString()
                                .replace(File.separatorChar, '.')
                                .replaceAll("\\.class$", "");
                        String pathAndName = configLocation + "." + simpleClassName;
                    try{
                        return Class.forName(pathAndName);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    })
                    .filter(c -> c.isAnnotationPresent(AppComponentsContainerConfig.class))
                    .toArray(Class<?>[]::new);
        }

    }

}
