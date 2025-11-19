package com.scentalux.service.impl;

import com.scentalux.model.Perfume;
import com.scentalux.repo.IGenericRepo;
import com.scentalux.repo.PerfumeRepository;
import com.scentalux.service.PerfumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImplPerfume extends ImplGenericService<Perfume, Integer> implements PerfumeService {

    private final PerfumeRepository repo;

    @Override
    protected IGenericRepo<Perfume, Integer> getRepo() {
        return repo;
    }

    @Override
    public Page<Perfume> listPage(Pageable pageable) {
        if (pageable == null) {
            throw new IllegalArgumentException("Pageable parameter must not be null");
        }
        return repo.findAll(pageable);
    }
}
