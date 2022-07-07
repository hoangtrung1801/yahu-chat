package dao;

import java.util.List;

public interface DAO<T, K> {

    List<T> read();

    T readById(K id);

    T create(T entity);

    T update(T entity);

    T delete(T entity);
}
