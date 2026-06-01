package payments_service.client;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

// 🎯 Apunta al nombre exacto que tiene tu microservicio de órdenes en Eureka
@FeignClient(name = "orders-service")
public interface OrderClient {

    // 🔌 Este método llamará a una ruta que crearemos en Órdenes para confirmar el pago
    @PutMapping("/api/orders/{id}/confirmar")
    ResponseEntity<?> confirmarOrden(@PathVariable("id") Long id);
}
