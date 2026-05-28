package inventory_service.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Table(name = "dishes")
@Data // Genera getters, setters, toString, etc. automáticamente
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private String category; // Ej: 'Entradas', 'Segundos', 'Bebidas'

    private Boolean active = true; // Para "ocultar" platos sin borrarlos de las órdenes viejas
}
