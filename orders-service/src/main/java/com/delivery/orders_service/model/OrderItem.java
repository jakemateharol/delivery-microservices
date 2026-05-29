package com.delivery.orders_service.model;



import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID del plato es obligatorio")
    private Long dishId; // Conexión directa con el inventario-service

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer quantity;

    @NotNull(message = "El precio unitario es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private Double price;
}
