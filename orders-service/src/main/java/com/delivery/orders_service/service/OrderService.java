package com.delivery.orders_service.service;

import com.delivery.orders_service.model.Order;
import com.delivery.orders_service.repository.OrderRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public Order save(Order order) {
        order.setStatus("PENDIENTE");
        return orderRepository.save(order);
    }

    public Order update(Long id, Order order) {
        order.setId(id);
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        orderRepository.deleteById(id);
    }
}