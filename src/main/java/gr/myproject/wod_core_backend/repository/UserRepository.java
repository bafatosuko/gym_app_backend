package gr.myproject.wod_core_backend.repository;

import gr.myproject.wod_core_backend.core.enums.Role;
import gr.myproject.wod_core_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> , JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findAllByRole(Role role);



}
