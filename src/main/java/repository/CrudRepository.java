package repository;

import java.util.List;

public interface CrudRepository <T> extends SuperDao{
    boolean add(T entity);
    boolean update(T entity);
    boolean delete(String id);
    T findById(String id);
    List<T> findAll();
}
