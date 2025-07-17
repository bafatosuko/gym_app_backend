package gr.myproject.wod_core_backend.dto;

import gr.myproject.wod_core_backend.core.enums.GenderType;
import gr.myproject.wod_core_backend.core.enums.Role;

import java.time.LocalDate;

public class UserReadOnlyDTO {

    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private LocalDate dateOfBirth;
    private GenderType gender;
    private Role role;
}
