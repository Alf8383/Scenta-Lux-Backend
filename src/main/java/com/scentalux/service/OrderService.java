package com.scentalux.service;

import com.scentalux.dto.CreateOrderDTO;
import com.scentalux.dto.OrderDTO;
import com.scentalux.model.Order;
import com.scentalux.exception.OrderNotFoundException;
import com.scentalux.exception.OrderValidationException;

import java.util.List;

public interface OrderService extends GenericService<Order, Integer> {

   
    OrderDTO createOrder(CreateOrderDTO orderDTO, String username) throws OrderValidationException;

    List<OrderDTO> getUserOrders(String username) throws OrderNotFoundException;

    OrderDTO getOrderByNumber(String orderNumber) throws OrderNotFoundException;

    OrderDTO updateOrderStatus(Integer orderId, String status) throws OrderValidationException;

    OrderDTO uploadReceipt(Integer orderId, String receiptImageUrl) throws OrderValidationException;
}
