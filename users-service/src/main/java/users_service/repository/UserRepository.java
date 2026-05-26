package users_service.users_service.repository;



import org.springframework.data.jpa.repository.JpaRepository;

import users_service.users_service.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
