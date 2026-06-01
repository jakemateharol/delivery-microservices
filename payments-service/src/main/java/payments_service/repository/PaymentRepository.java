package payments_service.repository;



import payments_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Hereda todos los métodos mágicos como save(), findById(), etc.
}
