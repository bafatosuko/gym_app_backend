package gr.myproject.wod_core_backend.service;

import gr.myproject.wod_core_backend.core.exceptions.AppObjectAlreadyExists;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectInvalidArgumentException;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectNotFoundException;
import gr.myproject.wod_core_backend.dto.CustomerInsertDTO;
import gr.myproject.wod_core_backend.dto.CustomerReadOnlyDTO;
import gr.myproject.wod_core_backend.mapper.Mapper;
import gr.myproject.wod_core_backend.model.Customer;
import gr.myproject.wod_core_backend.repository.CustomerRepository;
import gr.myproject.wod_core_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;
    private final Mapper mapper;
    private final UserRepository userRepository;


    //  ----- CREATE -----
    @Transactional(rollbackOn = {AppObjectAlreadyExists.class, IOException.class})
    public CustomerReadOnlyDTO saveCustomer(CustomerInsertDTO customerInsertDTO)
            throws AppObjectAlreadyExists, AppObjectInvalidArgumentException, IOException {

        String username = customerInsertDTO.getUser().getUsername();

        // --------- Έλεγχος αν υπάρχει χρήστης με το ίδιο username ---------
        if (userRepository.existsByUsername(username)) {
            LOGGER.warn("User with username {} already exists", username);
            throw new AppObjectAlreadyExists("User", "User with username " + username + " already exists");
        }

        try {
            // --------- Κάνε mapping το DTO σε entity ---------
            var customerEntity = mapper.mapCustomerToEntity(customerInsertDTO);

            // --------- Αποθήκευση ---------
            var savedCustomer = customerRepository.save(customerEntity);

            // --------- Επιστροφή DTO ---------
            return mapper.mapToCustomerReadOnlyDTO(savedCustomer);

        } catch (IllegalArgumentException ex) {
            LOGGER.error("Invalid customer data: {}", ex.getMessage());
            throw new AppObjectInvalidArgumentException("Customer", "Invalid customer data");
        }
    }


    // ----------- READ ALL -----------
    public List<CustomerReadOnlyDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(mapper::mapToCustomerReadOnlyDTO)
                .collect(Collectors.toList());
    }

    // ----------- READ BY ID -----------
    public CustomerReadOnlyDTO getCustomerById(Long id) throws AppObjectNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException( "Customer", "Customer with id " + id + " not found"));
        return mapper.mapToCustomerReadOnlyDTO(customer);
    }

    // ----------- DELETE BY ID -----------
    @Transactional
    public void deleteCustomerById(Long id) throws AppObjectNotFoundException {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("Customer" ,"Customer with id " + id + " not found"));

        customerRepository.delete(customer);
    }

    // ----------- EXISTS BY USERNAME -----------
    public boolean existsByUsername(String username) {
        return customerRepository.findByUserUsername(username).isPresent();
    }

    // ----------- FIND BY USERNAME -----------
    public Optional<CustomerReadOnlyDTO> findByUsername(String username) {
        return customerRepository.findByUserUsername(username)
                .map(mapper::mapToCustomerReadOnlyDTO);
    }

}
