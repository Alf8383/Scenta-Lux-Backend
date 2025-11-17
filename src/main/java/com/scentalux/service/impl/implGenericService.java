package com.scentalux.service.impl;

import com.scentalux.exception.ModelNotFoundException;
import com.scentalux.repo.IGenericRepo;
import com.scentalux.service.GenericService;
import java.util.List;

public abstract class implGenericService <T,ID> implements GenericService<T,ID> {
    protected abstract IGenericRepo<T, ID> getRepo();
    @Override
    public T save(T t) throws Exception {
        return getRepo().save(t);
    }

    @Override
    public T update(T t, ID id) throws Exception {
        // TODO verificar el id de la entidad
        getRepo().findById(id).orElseThrow(()-> new ModelNotFoundException("ID NOT FOUND: " + id));
        return getRepo().save(t);
    }

    @Override
    public List<T> findAll() throws Exception {
        return getRepo().findAll();
    }

    @Override
    public T findById(ID id) throws Exception {
        return getRepo().findById(id).orElseThrow(()-> new ModelNotFoundException("ID NOT FOUND: " + id));
    }

    @Override
    public void delete(ID id) throws Exception {
        getRepo().findById(id).orElseThrow(()-> new ModelNotFoundException("ID NOT FOUND: " + id));
        getRepo().deleteById(id);
    }
}
