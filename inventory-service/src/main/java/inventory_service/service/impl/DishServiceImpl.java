package inventory_service.service.impl;

import inventory_service.entity.Dish;
import inventory_service.repository.DishRepository;
import inventory_service.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishRepository dishRepository;

    @Override
    public Dish crearPlato(Dish dish) {
        if(dishRepository.findAll().stream().anyMatch(d -> d.getName().equalsIgnoreCase(dish.getName()))) {
            throw new RuntimeException("El plato '" + dish.getName() + "' ya existe en el menú.");
        }
        return dishRepository.save(dish);
    }

    @Override
    public List<Dish> obtenerCatalogo() {
        return dishRepository.findByActiveTrue(); // Solo lo disponible
    }

    @Override
    public Dish obtenerPlatoPorId(Long id) {
        return dishRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado con ID: " + id));
    }

    @Override
    public Dish actualizarStock(Long id, Integer nuevoStock) {
        Dish dish = obtenerPlatoPorId(id);
        dish.setStock(nuevoStock);
        return dishRepository.save(dish);
    }
    @Override
public Dish descontarStock(Long id, Integer cantidad) {
    Dish dish = obtenerPlatoPorId(id);

    // 1. Validar si hay suficiente stock disponible
    if (dish.getStock() < cantidad) {
        throw new RuntimeException("Stock insuficiente para el plato '" + dish.getName() + "'. Stock actual: " + dish.getStock());
    }

    // 2. Restar el stock
    int nuevoStock = dish.getStock() - cantidad;
    dish.setStock(nuevoStock);

    // 3. LÓGICA AUTOMÁTICA: Si llega a 0, se pasa a agotado (active = false)
    if (nuevoStock == 0) {
        dish.setActive(false);
        System.out.println("🚨 ALERTA ADMINISTRADOR: El plato '" + dish.getName() + "' se ha AGOTADO por completo.");
    } 
    // 4. LÓGICA AUTOMÁTICA: Alerta de stock mínimo (ejemplo: 3 unidades o menos)
    else if (nuevoStock <= 3) {
        System.out.println("⚠️ ADVERTENCIA ADMINISTRADOR: El plato '" + dish.getName() + "' tiene stock crítico. Solo quedan " + nuevoStock + " unidades.");
    }

    return dishRepository.save(dish);
}
}