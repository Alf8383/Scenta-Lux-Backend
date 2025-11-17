package com.scentalux.service;

import java.util.List;

public interface GenericService<T, I> {

    T save(T t);

    T update(T t, I id);

    List<T> findAll();

    T findById(I id);

    void delete(I id);
}
