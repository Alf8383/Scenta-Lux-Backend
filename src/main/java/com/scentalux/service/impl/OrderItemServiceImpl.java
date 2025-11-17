package com.scentalux.service.impl;

import com.scentalux.model.OrderItem;
import com.scentalux.repo.IOrderItemRepo;
import com.scentalux.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl extends implGenericService<OrderItem, Integer> implements OrderItemService {

    private final IOrderItemRepo repo;

    @Override
    protected IOrderItemRepo getRepo() {
        return repo;
    }
}