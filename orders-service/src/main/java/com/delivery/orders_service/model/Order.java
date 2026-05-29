package com.delivery.orders_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter 
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El cliente no puede estar vacío")
    private String customerName;

    @NotBlank(message = "La dirección no puede estar vacía")
    private String address;

    // Relación Uno a Muchos: Una orden tiene muchos platos/detalles
    @NotEmpty(message = "La orden debe contener al menos un plato")
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id") // Crea la llave foránea en la tabla order_items
    private List<OrderItem> items;

    private Double total;

    private String status; // PENDIENTE, EN_RUTA, ENTREGADO, CANCELADO
}