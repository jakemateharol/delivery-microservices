package inventory_service.repository;

import inventory_service.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DishRepository extends JpaRepository<Dish, Long> {
    // Para que el cliente solo vea en la carta los platos disponibles y activos
    List<Dish> findByActiveTrue();
}
