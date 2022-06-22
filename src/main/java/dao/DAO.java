package dao;

import javax.persistence.EntityManager;
import java.util.List;

public interface DAO<T, K> {

    List<T> read();

    T readById(K id);

    T create(T entity);

    T update(T entity);

    T delete(T entity);

//    void persist(T entity);
}
