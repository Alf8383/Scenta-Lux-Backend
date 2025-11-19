package com.scentalux.service.impl;

import org.springframework.stereotype.Service;

import com.scentalux.model.User;
import com.scentalux.repo.IGenericRepo;
import com.scentalux.repo.IUserRepo;
import com.scentalux.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import lombok.NonNull;

@Service
@RequiredArgsConstructor
public class implUsuario extends ImplGenericService<User, Integer> implements UsuarioService {
        private final IUserRepo repo;

    @Override
    protected IGenericRepo<User, Integer> getRepo(){
        return repo;
    }
    
    @Override
    public Page<User> listPage(@NonNull Pageable pageable) {
        return repo.findAll(pageable);
    }
    
}
