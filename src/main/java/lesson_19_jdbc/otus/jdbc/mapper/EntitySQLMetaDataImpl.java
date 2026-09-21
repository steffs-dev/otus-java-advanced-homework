package lesson_19_jdbc.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData<T> {
    private final EntityClassMetaData<T> classMetaData;
    private final String tableName;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> classMetaData) {
        this.classMetaData = classMetaData;
        tableName = classMetaData.getName();
    }

    @Override
    public EntityClassMetaData<T> getClassMetaData() {
        return classMetaData;
    }

    @Override
    public String getSelectAllSql() {
        return "SELECT * FROM " + tableName + ";";
    }

    @Override
    public String getSelectByIdSql() {
        StringBuilder sql = new StringBuilder("SELECT * FROM ");
        sql.append(tableName).append(" WHERE ");

        String idFieldName = classMetaData.getIdField().getName().toLowerCase();
        sql.append(idFieldName).append(" = ?;");

        return sql.toString();
    }

    @Override
    public String getInsertSql() {
        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(tableName).append(" (");

        List<Field> fieldsWithoutId = classMetaData.getFieldsWithoutId();
        sql.append(getColumnsWithoutIdForSql(fieldsWithoutId, false));
        sql.append(") VALUES (");
        sql.append("?, ".repeat(fieldsWithoutId.size()));
        sql.setLength(sql.length() - 2);
        sql.append(");");

        return sql.toString();
    }

    @Override
    public String getUpdateSql() {
        StringBuilder sql = new StringBuilder("UPDATE ");
        sql.append(tableName).append(" SET ");
        sql.append(getColumnsWithoutIdForSql(classMetaData.getFieldsWithoutId(), true));
        sql.append(" WHERE ");
        String idFieldName = classMetaData.getIdField().getName().toLowerCase();
        sql.append(idFieldName).append(" = ?;");
        return sql.toString();
    }

    private String getColumnsWithoutIdForSql(List<Field> fieldsWithoutId, boolean questionMarkToAdd) {
        StringBuilder sql = new StringBuilder();
        for (Field field : fieldsWithoutId) {
            sql.append(field.getName());
            sql.append(questionMarkToAdd ? "=?" : "");
            sql.append(", ");
        }
        sql.setLength(sql.length() - 2);
        return sql.toString();
    }
}
