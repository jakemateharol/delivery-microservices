package inventory_service.controller;

import inventory_service.entity.Dish;
import inventory_service.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    // 1. Crear un nuevo plato (Ideal para el ADMIN)
    @PostMapping
    public ResponseEntity<?> registrarPlato(@RequestBody Dish dish) {
        try {
            Dish nuevoPlato = dishService.crearPlato(dish);
            return new ResponseEntity<>(nuevoPlato, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }

    // 2. Ver la carta/catálogo (Para el CLIENTE y todos)
    @GetMapping
    public ResponseEntity<?> verCarta() {
        return ResponseEntity.ok(dishService.obtenerCatalogo());
    }

    // 3. Actualizar el stock de un plato (Para cuando se vayan vendiendo)
    // 4. Descontar stock automáticamente al hacer un pedido
    @PutMapping("/{id}/descontar")
    public ResponseEntity<?> descontarStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        try {
            Dish dishActualizado = dishService.descontarStock(id, cantidad);
            return ResponseEntity.ok(dishActualizado);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}