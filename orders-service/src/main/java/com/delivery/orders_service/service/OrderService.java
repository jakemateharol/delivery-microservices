package com.delivery.orders_service.service;

import com.delivery.orders_service.model.Order;
import com.delivery.orders_service.model.OrderItem;
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

    // Guardar Orden calculando el Total automáticamente
    public Order save(Order order) {
        order.setStatus("PENDIENTE");
        
        // 🎯 Calcular el total recorriendo los platos del carrito
        double totalCalculado = order.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        
        order.setTotal(totalCalculado);
        return orderRepository.save(order);
    }

    // Actualizar solo el estado de la orden (Ideal para el Repartidor/Admin)
    public Order cambiarEstado(Long id, String nuevoEstado) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
        
        order.setStatus(nuevoEstado.toUpperCase());
        return orderRepository.save(order);
    }

    // Borrado Lógico: No destruye el registro, lo cancela
    public void cancelarOrden(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
        order.setStatus("CANCELADO");
        orderRepository.save(order);
    }
}