package com.scentalux.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IGenericRepo<T,I> extends JpaRepository<T,I> {

}
