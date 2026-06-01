package payments_service.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;       // El ID de la orden que viene desde orders-service
    private Double amount;       // El monto total que se está cobrando
    private String status;       // "APROBADO" o "RECHAZADO"
    private String cardNumber;   // Solo para simular, guardaremos los últimos 4 dígitos
    private LocalDateTime transactionDate;

    // --- CONSTRUCTORES ---
    public Payment() {}

    public Payment(Long orderId, Double amount, String status, String cardNumber, LocalDateTime transactionDate) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = status;
        this.cardNumber = cardNumber;
        this.transactionDate = transactionDate;
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
}
