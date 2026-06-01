package com.delivery.orders_service.service;

import com.delivery.orders_service.client.InventoryClient; // 🎯 Importamos el cliente
import com.delivery.orders_service.model.Order;
import com.delivery.orders_service.model.OrderItem;
import com.delivery.orders_service.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient; // 🎯 Declaramos el puente

    // Lo agregamos al constructor para la inyección de dependencias
    public OrderService(OrderRepository orderRepository, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    @Transactional
    public Order save(Order order) {
        // 🛒 MEJORA: La orden nace esperando que el cliente pague en la pantalla de pasarela
        order.setStatus("PENDIENTE_PAGO");
        
        // 1. Calcular el total automáticamente
        double totalCalculado = order.getItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getPrice())
                .sum();
        order.setTotal(totalCalculado);

        // 🛑 QUITAMOS EL DESCUENTO DE STOCK DE AQUÍ. Ahora se hace tras confirmar el pago.
        
        // 3. Guardar la orden en la BD de órdenes
        return orderRepository.save(order);
    }

    // 💳 NUEVO MÉTODO: Este es el que mandará a llamar payment-service mediante Feign
    @Transactional // 🛡️ Si falla el inventario, el pago no confirma la orden (Rollback)
    public Order confirmarYDescontarStock(Long orderId) {
        // 1. Buscar la orden en la base de datos
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No se encontró la orden con ID: " + orderId));

        // 2. Validar que no haya sido procesada antes
        if ("PAGADO".equals(order.getStatus())) {
            throw new RuntimeException("Esta orden ya ha sido pagada y procesada previamente.");
        }

        // 3. 🔥 LA MAGIA DE FEIGN: Recorrer cada plato y restar stock en el Inventario RECIÉN AHORA
        for (OrderItem item : order.getItems()) {
            inventoryClient.descontarStock(item.getDishId(), item.getQuantity());
        }

        // 4. Cambiar el estado de la orden a PAGADO
        order.setStatus("PAGADO");

        // 5. Guardar los cambios finales
        return orderRepository.save(order);
    }

    public Order cambiarEstado(Long id, String nuevoEstado) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
        
        order.setStatus(nuevoEstado.toUpperCase());
        return orderRepository.save(order);
    }

    public void cancelarOrden(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
        order.setStatus("CANCELADO");
        orderRepository.save(order);
    }
}