package lesson_19_jdbc.otus.jdbc.mapper;

/**
 * Создает SQL - запросы
 */
public interface EntitySQLMetaData<T> {
    EntityClassMetaData<T> getClassMetaData();

    String getSelectAllSql();

    String getSelectByIdSql();

    String getInsertSql();

    String getUpdateSql();
}
