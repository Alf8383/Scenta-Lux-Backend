package com.scentalux.service.impl;

import org.springframework.stereotype.Service;

import com.scentalux.model.Role;
import com.scentalux.repo.IGenericRepo;
import com.scentalux.repo.RolRepository;
import com.scentalux.service.RolService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
@RequiredArgsConstructor
public class implRol extends implGenericService<Role, Integer> implements RolService {
    private final RolRepository repo;

    @Override
    protected IGenericRepo<Role, Integer> getRepo(){
        return repo;
    }
    @Override
    public Page<Role> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }

}
