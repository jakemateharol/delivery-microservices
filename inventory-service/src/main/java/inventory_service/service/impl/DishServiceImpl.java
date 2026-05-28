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
}