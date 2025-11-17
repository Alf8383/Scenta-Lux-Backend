package com.scentalux.service;

import com.scentalux.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface  RolService extends GenericService<Role, Integer>{
    Page<Role> listPage(Pageable pageable);
}
