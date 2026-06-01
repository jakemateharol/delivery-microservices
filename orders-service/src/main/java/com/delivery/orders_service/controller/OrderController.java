package com.delivery.orders_service.controller;

import com.delivery.orders_service.model.Order;
import com.delivery.orders_service.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus; // 🎯 NUEVO IMPORT: Corrige el error de HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map; // 🎯 NUEVO IMPORT: Corrige el error de Map

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<Order> getAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Order create(@Valid @RequestBody Order order) {
        return orderService.save(order);
    }

    // Endpoint clave para que el Admin o el Repartidor cambien el estado (Ej: /api/orders/1/status?nuevoEstado=EN_RUTA)
    @PutMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @RequestParam String nuevoEstado) {
        return orderService.cambiarEstado(id, nuevoEstado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderService.cancelarOrden(id);
        return ResponseEntity.noContent().build();
    }

    // 🎯 Esta es la ruta exacta que pusimos en el OrderClient de Pagos
    @PutMapping("/{id}/confirmar")
    public ResponseEntity<?> confirmarOrden(@PathVariable Long id) {
        try {
            Order ordenActualizada = orderService.confirmarYDescontarStock(id);
            return ResponseEntity.ok(ordenActualizada);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}