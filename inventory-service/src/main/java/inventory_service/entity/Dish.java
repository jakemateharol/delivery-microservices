package inventory_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; // 🎯 Importante para las validaciones
import lombok.*;

@Entity
@Table(name = "dishes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre del plato no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Column(nullable = false, length = 255)
    private String description;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser un número positivo mayor a 0") // 🛑 No precios negativos ni cero
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser menor a cero") // 🛑 No stock negativo
    @Column(nullable = false)
    private Integer stock;

    @NotBlank(message = "La categoría es obligatoria")
    @Column(nullable = false, length = 50)
    private String category;

    private boolean active = true; // Por defecto inicia activo
}