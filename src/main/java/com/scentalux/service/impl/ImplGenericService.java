package com.scentalux.service.impl;

import com.scentalux.exception.ModelNotFoundException;
import com.scentalux.repo.IGenericRepo;
import com.scentalux.service.GenericService;
import java.util.List;

public abstract class ImplGenericService<T, I> implements GenericService<T, I> {

    protected abstract IGenericRepo<T, I> getRepo();

    private static final String ID_NOT_FOUND_MSG = "ID NOT FOUND: ";

    @Override
    public T save(T t) {
        return getRepo().save(t);
    }

    @Override
    public T update(T t, I id) {
        getRepo().findById(id)
            .orElseThrow(() -> new ModelNotFoundException(ID_NOT_FOUND_MSG + id));
        return getRepo().save(t);
    }

    @Override
    public List<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public T findById(I id) {
        return getRepo().findById(id)
            .orElseThrow(() -> new ModelNotFoundException(ID_NOT_FOUND_MSG + id));
    }

    @Override
    public void delete(I id) {
        getRepo().findById(id)
            .orElseThrow(() -> new ModelNotFoundException(ID_NOT_FOUND_MSG + id));
        getRepo().deleteById(id);
    }
}
