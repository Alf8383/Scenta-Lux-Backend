package com.scentalux.service;

import com.scentalux.model.Perfume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PerfumeService extends GenericService<Perfume, Integer> {
    Page<Perfume> listPage(Pageable pageable);
}
