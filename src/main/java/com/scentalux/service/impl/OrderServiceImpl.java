package com.scentalux.service.impl;

import com.scentalux.dto.CreateOrderDTO;
import com.scentalux.dto.OrderDTO;
import com.scentalux.dto.OrderItemDTO;
import com.scentalux.dto.OrderItemResponseDTO;
import com.scentalux.model.Order;
import com.scentalux.model.OrderItem;
import com.scentalux.model.Perfume;
import com.scentalux.model.User;
import com.scentalux.repo.IOrderRepo;
import com.scentalux.repo.IUserRepo;
import com.scentalux.repo.PerfumeRepository;
import com.scentalux.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends implGenericService<Order, Integer> implements OrderService {

    private final IOrderRepo orderRepo;
    private final IUserRepo userRepo;
    private final PerfumeRepository perfumeRepo;

    @Override
    protected IOrderRepo getRepo() {
        return orderRepo;
    }

    @Transactional
    @Override
    public OrderDTO createOrder(CreateOrderDTO orderDTO, String username) throws Exception {
        // Buscar usuario - usando tu método actual que devuelve User en lugar de Optional
        User user = userRepo.findOneByUsername(username);
        if (user == null) {
            throw new Exception("Usuario no encontrado: " + username);
        }

        // Crear pedido
        Order order = new Order();
        order.setUser(user);
        order.setPaymentMethod(orderDTO.getPaymentMethod());
        order.setCustomerName(orderDTO.getCustomerName());
        order.setShippingAddress(orderDTO.getShippingAddress());
        order.setCity(orderDTO.getCity());
        order.setPostalCode(orderDTO.getPostalCode());
        order.setPhone(orderDTO.getPhone());

        // ✅ ESTABLECER ESTADO INICIAL COMO PENDIENTE
        order.setStatus("PENDIENTE");

        // Calcular totales
        double subtotal = 0.0;

        // Procesar items
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            Optional<Perfume> perfumeOpt = perfumeRepo.findById(itemDTO.getPerfumeId());
            if (perfumeOpt.isEmpty()) {
                throw new Exception("Perfume no encontrado: " + itemDTO.getPerfumeId());
            }
            
            Perfume perfume = perfumeOpt.get();

            // Verificar stock
            if (perfume.getStock() < itemDTO.getQuantity()) {
                throw new Exception("Stock insuficiente para: " + perfume.getName() + 
                                  ". Stock disponible: " + perfume.getStock() + 
                                  ", solicitado: " + itemDTO.getQuantity());
            }

            // Actualizar stock
            perfume.setStock(perfume.getStock() - itemDTO.getQuantity());
            perfumeRepo.save(perfume);

            // Crear item del pedido
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPerfume(perfume);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setUnitPrice(perfume.getPrice());
            orderItem.setTotalPrice(perfume.getPrice() * itemDTO.getQuantity());

            order.getItems().add(orderItem);
            subtotal += orderItem.getTotalPrice();
        }

        // Calcular impuestos y total
        double taxes = subtotal * 0.08; // 8% de impuestos
        double total = subtotal + taxes;

        order.setSubtotal(subtotal);
        order.setTaxes(taxes);
        order.setTotal(total);

        // Guardar pedido
        Order savedOrder = orderRepo.save(order);
        return convertToDTO(savedOrder);
    }

    @Override
    public List<OrderDTO> getUserOrders(String username) throws Exception {
        List<Order> orders = orderRepo.findByUsernameOrderByOrderDateDesc(username);
        return orders.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderByNumber(String orderNumber) throws Exception {
        Optional<Order> orderOpt = orderRepo.findByOrderNumber(orderNumber);
        if (orderOpt.isEmpty()) {
            throw new Exception("Pedido no encontrado: " + orderNumber);
        }
        return convertToDTO(orderOpt.get());
    }

    @Transactional
    @Override
    public OrderDTO updateOrderStatus(Integer orderId, String status) throws Exception {
        Order order = findById(orderId);
        order.setStatus(status);
        Order updatedOrder = orderRepo.save(order);
        return convertToDTO(updatedOrder);
    }

    @Transactional
    @Override
    public OrderDTO uploadReceipt(Integer orderId, String receiptImageUrl) throws Exception {
        Order order = findById(orderId);
        order.setReceiptImageUrl(receiptImageUrl);
       // order.setStatus("CONFIRMADO");
        Order updatedOrder = orderRepo.save(order);
        return convertToDTO(updatedOrder);
    }

    private OrderDTO convertToDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getOrderNumber());
        dto.setCustomerName(order.getCustomerName());
        dto.setOrderDate(order.getOrderDate());
        dto.setSubtotal(order.getSubtotal());
        dto.setTaxes(order.getTaxes());
        dto.setTotal(order.getTotal());
        dto.setStatus(order.getStatus());
        dto.setPaymentMethod(order.getPaymentMethod());
        dto.setReceiptImageUrl(order.getReceiptImageUrl());
        dto.setShippingAddress(order.getShippingAddress());
        dto.setCity(order.getCity());
        dto.setPostalCode(order.getPostalCode());

        // Convertir items
        List<OrderItemResponseDTO> itemDTOs = order.getItems().stream()
                .map(item -> {
                    OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
                    itemDTO.setPerfumeId(item.getPerfume().getId());
                    itemDTO.setPerfumeName(item.getPerfume().getName());
                    itemDTO.setBrand(item.getPerfume().getBrand());
                    itemDTO.setImageUrl(item.getPerfume().getImageUrl());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setUnitPrice(item.getUnitPrice());
                    itemDTO.setTotalPrice(item.getTotalPrice());
                    return itemDTO;
                })
                .collect(Collectors.toList());

        dto.setItems(itemDTOs);
        return dto;
    }
}