package users_service.security;



import users_service.entity.Role;
import users_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSetup implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Si la tabla de roles está vacía, insertamos los roles por defecto para el delivery
        if (roleRepository.count() == 0) {
            Role cliente = new Role();
            cliente.setName("ROLE_CLIENTE");
            roleRepository.save(cliente);

            Role admin = new Role();
            admin.setName("ROLE_ADMIN");
            roleRepository.save(admin);

            Role repartidor = new Role();
            repartidor.setName("ROLE_REPARTIDOR");
            roleRepository.save(repartidor);
            
            System.out.println("--> ¡Roles iniciales del delivery insertados con éxito!");
        }
    }
}
