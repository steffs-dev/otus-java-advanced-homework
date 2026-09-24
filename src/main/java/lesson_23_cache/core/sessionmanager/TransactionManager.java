package lesson_23_cache.core.sessionmanager;

import lesson_22_jpql.core.sessionmanager.TransactionAction;

public interface TransactionManager {

    <T> T doInTransaction(lesson_22_jpql.core.sessionmanager.TransactionAction<T> action);

    <T> T doInReadOnlyTransaction(TransactionAction<T> action);
}
