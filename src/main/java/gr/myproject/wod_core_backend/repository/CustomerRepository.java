package gr.myproject.wod_core_backend.repository;

import gr.myproject.wod_core_backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> , JpaSpecificationExecutor<Customer> {

    Optional<Customer> findByUserUsername(String username);


    List<Customer> findAllByIsSubscribedTrue();

}
