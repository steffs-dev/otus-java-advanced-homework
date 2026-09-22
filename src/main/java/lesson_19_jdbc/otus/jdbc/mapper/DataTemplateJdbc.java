package lesson_19_jdbc.otus.jdbc.mapper;

import lesson_19_jdbc.otus.core.repository.DataTemplate;
import lesson_19_jdbc.otus.core.repository.executor.DbExecutor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
@SuppressWarnings({"java:S1068"})
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData<T> entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final List<Field> fields;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData<T> entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
        this.entityClassMetaData = entitySQLMetaData.getClassMetaData();
        this.fields = entityClassMetaData.getAllFields();
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        String sql = entitySQLMetaData.getSelectByIdSql();
        return dbExecutor.executeSelect(connection, sql, List.of(id), rs -> {
            try {
                if (rs.next()) {

                    return rowMapper(rs);
                }
            } catch (SQLException e) {
                throw new UnsupportedOperationException();
            }
            return null;
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        String sql = entitySQLMetaData.getSelectAllSql();

        return dbExecutor.executeSelect(connection, sql, Collections.emptyList(), rs -> {
            List<T> entityList = new ArrayList<>();
            try {
                while (rs.next()) {
                    entityList.add(rowMapper(rs));
                }
                return entityList;
            } catch (SQLException e) {
                throw new UnsupportedOperationException();
            }
        }).orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T entity) {
        try {
            String sql = entitySQLMetaData.getInsertSql();
            return dbExecutor.executeStatement(connection, sql, getParamsList(entity));
        } catch (Exception e) {
            throw new UnsupportedOperationException("update failed for " + entity, e);
        }
    }

    @Override
    public void update(Connection connection, T entity) {
        try {
            Object id = entityClassMetaData.getIdField().get(entity);
            List<Object> params = new ArrayList<>(getParamsList(entity));
            params.add(id);
            String sql = entitySQLMetaData.getUpdateSql();
            dbExecutor.executeStatement(connection, sql, params);
        } catch (Exception e) {
            throw new UnsupportedOperationException("update failed for " + entity, e);
        }
    }

    private T rowMapper(ResultSet rs) {
        Object[] params = fields.stream().map(field -> {
            try {
                return rs.getObject(field.getName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }).toArray(Object[]::new);

        try {
            return entityClassMetaData.getConstructor()
                    .newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new UnsupportedOperationException(e);
        }

    }

    private List<Object> getParamsList(T entity) {
        List<Field> paramFields = entityClassMetaData.getFieldsWithoutId();
        return paramFields.stream().map(field -> {
            try {
                field.setAccessible(true);
                return field.get(entity);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}
