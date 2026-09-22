package lesson_19_jdbc.otus.jdbc.mapper;

import lesson_19_jdbc.otus.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Class<T> entityClass;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;
    private final Field idField;

    private final Constructor<T> constructor;

    public EntityClassMetaDataImpl() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
        this.allFields = initAllFields();
        this.idField = initIdField();
        this.fieldsWithoutId = initFieldsWithoutId();
        this.constructor = initAllArgsConstructor();
    }

    private List<Field> initAllFields() {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(f -> !Modifier.isStatic(f.getModifiers()))
                .peek(f -> f.setAccessible(true))
                .toList();
    }

    private Field initIdField() {
        return allFields.stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException(
                        "Нет поля с @Id у " + entityClass.getName()));
    }

    private List<Field> initFieldsWithoutId() {
        return allFields.stream()
                .filter(f -> !f.equals(idField))
                .toList();
    }

    private Constructor<T> initAllArgsConstructor() {
        Class[] paramTypes = allFields.stream()
                .map(Field::getType)
                .toArray(Class[]::new);
        try {
            Constructor<T> constructor = entityClass.getDeclaredConstructor(paramTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("У класса " + entityClass.getName() +
                    " нет конструктора без параметров", e);
        }
    }

    @Override
    public Class<T> getEntityClass() {
        return entityClass;
    }

    @Override
    public String getName() {
        return entityClass.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }

}
