package gr.myproject.wod_core_backend.mapper;

import gr.myproject.wod_core_backend.dto.*;
import gr.myproject.wod_core_backend.model.Customer;
import gr.myproject.wod_core_backend.model.Trainer;
import gr.myproject.wod_core_backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Mapper {

    private final PasswordEncoder passwordEncoder;


     // ----  CUSTOMER !!!
    public Customer mapCustomerToEntity(CustomerInsertDTO dto) {


        User user = new User();
        user.setUsername(dto.getUser().getUsername());
        user.setPassword(passwordEncoder.encode(dto.getUser().getPassword()));
        user.setFirstname(dto.getUser().getFirstname());
        user.setLastname(dto.getUser().getLastname());
        user.setDateOfBirth(dto.getUser().getDateOfBirth());
        user.setGender(dto.getUser().getGender());
        user.setRole(dto.getUser().getRole());
        user.setIsActive(dto.getIsActive());


        Customer customer = new Customer();
        customer.setIsActive(dto.getIsActive());
        customer.setIsSubscribed(dto.getIsSubscribed());
        customer.setUser(user);

        return customer;
    }

    public CustomerReadOnlyDTO mapToCustomerReadOnlyDTO (Customer customer) {
        // setting user μεσα στον customer  !!
        UserReadOnlyDTO userDTO = new UserReadOnlyDTO();
        userDTO.setUsername(customer.getUser().getUsername());
        userDTO.setFirstname(customer.getUser().getFirstname());
        userDTO.setLastname(customer.getUser().getLastname());
        userDTO.setDateOfBirth(customer.getUser().getDateOfBirth());
        userDTO.setGender(customer.getUser().getGender());
        userDTO.setRole(customer.getUser().getRole());

        // CUSTOMER !!
        CustomerReadOnlyDTO dto = new CustomerReadOnlyDTO();
        dto.setId(customer.getId());
        dto.setUuid(customer.getUuid());
        dto.setIsActive(customer.getIsActive());
        dto.setIsSubscribed(customer.getIsSubscribed());

        // Setting userDTO sto Customer
        dto.setUser(userDTO);
        return dto;
    }


    // ---------- TRAINER ----------

//    public Trainer mapToTrainerEntity(TrainerInsertDTO dto) {
//        return Trainer.builder()
//                .isActive(dto.getIsActive())
//                .user(mapToUserEntity(dto.getUser()))
//                .build();
//    }
//
//    public TrainerReadOnlyDTO mapToTrainerReadOnlyDTO(Trainer trainer) {
//        TrainerReadOnlyDTO dto = new TrainerReadOnlyDTO();
//        dto.setId(trainer.getId());
//        dto.setUuid(trainer.getUuid());
//        dto.setIsActive(trainer.getIsActive());
//        dto.setUser(mapToUserReadOnlyDTO(trainer.getUser()));
//        return dto;
  //  }

}
