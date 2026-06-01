package payments_service.service;



import payments_service.client.OrderClient;
import payments_service.entity.Payment;
import payments_service.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderClient orderClient; // Inyectamos el puente Feign

    public Payment procesarPago(Long orderId, Double amount, String cardNumber) {
        // 💳 Simulación de lógica de negocio: Si la tarjeta empieza con "4" pasa, si no, se rechaza
        String status = "APROBADO";
        if (cardNumber == null || cardNumber.length() < 4) {
            status = "RECHAZADO";
        }

        // Guardamos los últimos 4 dígitos para proteger la privacidad
        String ultimosDigitos = (cardNumber != null && cardNumber.length() >= 4) 
                ? cardNumber.substring(cardNumber.length() - 4) 
                : "0000";

        // 1. Guardar el registro del intento de pago en la base de datos local de Pagos
        Payment payment = new Payment(orderId, amount, status, ultimosDigitos, LocalDateTime.now());
        Payment paymentGuardado = paymentRepository.save(payment);

        // 2. SI EL PAGO FUE APROBADO, le avisamos a órdenes por Feign
        if ("APROBADO".equals(status)) {
            try {
                orderClient.confirmarOrden(orderId);
            } catch (Exception e) {
                // Si falla la comunicación, lanzamos un error para enterarnos en consola
                throw new RuntimeException("El pago se cobró pero no se pudo notificar al servicio de órdenes: " + e.getMessage());
            }
        } else {
            throw new RuntimeException("El pago fue rechazado por la entidad bancaria.");
        }

        return paymentGuardado;
    }
}
