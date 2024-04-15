package ru.fildv.openclassroomdb.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<E, I> {
    List<E> findAll();

    Optional<E> findById(I id);

    boolean delete(I id);

    void update(E entity);

    E save(E entity);
}
