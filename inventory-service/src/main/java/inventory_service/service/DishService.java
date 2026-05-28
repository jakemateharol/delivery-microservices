package inventory_service.service;

import inventory_service.entity.Dish;
import java.util.List;

public interface DishService {
    Dish crearPlato(Dish dish);
    List<Dish> obtenerCatalogo();
    Dish obtenerPlatoPorId(Long id);
    Dish actualizarStock(Long id, Integer nuevoStock);
    Dish descontarStock(Long id, Integer cantidad);
}
