package com.scentalux.repo;

import com.scentalux.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IOrderItemRepo extends IGenericRepo<OrderItem, Integer> {
}