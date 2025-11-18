package com.scentalux.controller;

import com.scentalux.dto.CreateOrderDTO;
import com.scentalux.dto.OrderDTO;
import com.scentalux.dto.OrderItemResponseDTO;
import com.scentalux.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(
            @Valid @RequestBody CreateOrderDTO orderDTO,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            OrderDTO createdOrder = orderService.createOrder(orderDTO, username);
            return ResponseEntity.ok(createdOrder);
        } catch (Exception e) {
            // Return Map<String, String> only for error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new OrderDTO()); // Return an empty OrderDTO in case of error
        }
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderDTO>> getUserOrders(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<OrderDTO> orders = orderService.getUserOrders(username);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            // Return Map<String, String> only for error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(List.of()); // Return an empty list of orders in case of error
        }
    }

    @GetMapping("/{orderNumber}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable String orderNumber) {
        try {
            OrderDTO order = orderService.getOrderByNumber(orderNumber);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            // Return Map<String, String> only for error response
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new OrderDTO()); // Return an empty OrderDTO in case of error
        }
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Integer orderId,
            @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            OrderDTO updatedOrder = orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            // Return Map<String, String> only for error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new OrderDTO()); // Return an empty OrderDTO in case of error
        }
    }

    @PutMapping("/{orderId}/receipt")
    public ResponseEntity<OrderDTO> uploadReceipt(
            @PathVariable Integer orderId,
            @RequestBody Map<String, String> request) {
        try {
            String receiptImageUrl = request.get("receiptImageUrl");
            OrderDTO updatedOrder = orderService.uploadReceipt(orderId, receiptImageUrl);
            return ResponseEntity.ok(updatedOrder);
        } catch (Exception e) {
            // Return Map<String, String> only for error response
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new OrderDTO()); // Return an empty OrderDTO in case of error
        }
    }

    // Endpoint para obtener todos los pedidos (solo admin)
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        try {
            List<OrderDTO> orders = orderService.findAll().stream()
                    .map(order -> {
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
                                .toList(); // Use Stream.toList() here

                        dto.setItems(itemDTOs);
                        return dto;
                    })
                    .toList(); // Use Stream.toList() here
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            // Return Map<String, String> only for error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of()); // Return an empty list of orders in case of error
        }
    }

    // Endpoint para eliminar pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        try {
            orderService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // Return Map<String, String> only for error response
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Return no content on error
        }
    }
}
