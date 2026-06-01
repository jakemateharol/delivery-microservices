package payments_service.controller;



import payments_service.entity.Payment;
import payments_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> registrarPago(@RequestBody Map<String, Object> request) {
        try {
            // Extraemos los datos del JSON enviado desde Postman o JS
            Long orderId = Long.valueOf(request.get("orderId").toString());
            Double amount = Double.valueOf(request.get("amount").toString());
            String cardNumber = request.get("cardNumber").toString();

            Payment nuevoPago = paymentService.procesarPago(orderId, amount, cardNumber);
            return new ResponseEntity<>(nuevoPago, HttpStatus.CREATED);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
    }
}
