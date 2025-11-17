package com.scentalux.repo;

import com.scentalux.model.Order;
import com.scentalux.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface IOrderRepo extends IGenericRepo<Order, Integer> {
    
    List<Order> findByUserOrderByOrderDateDesc(User user);
    
    Optional<Order> findByOrderNumber(String orderNumber);
    
    @Query("SELECT o FROM Order o WHERE o.user.username = :username ORDER BY o.orderDate DESC")
    List<Order> findByUsernameOrderByOrderDateDesc(@Param("username") String username);
    
    @Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'PENDIENTE'")
    Long countPendingOrders();
}