package com.scentalux.service;

import com.scentalux.dto.CreateOrderDTO;
import com.scentalux.dto.OrderDTO;
import com.scentalux.model.Order;

import java.util.List;

public interface OrderService extends GenericService<Order, Integer> {
    
    OrderDTO createOrder(CreateOrderDTO orderDTO, String username) throws Exception;
    
    List<OrderDTO> getUserOrders(String username) throws Exception;
    
    OrderDTO getOrderByNumber(String orderNumber) throws Exception;
    
    OrderDTO updateOrderStatus(Integer orderId, String status) throws Exception;
    
    OrderDTO uploadReceipt(Integer orderId, String receiptImageUrl) throws Exception;
}