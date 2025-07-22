package gr.myproject.wod_core_backend.dto;

import gr.myproject.wod_core_backend.core.enums.GenderType;
import gr.myproject.wod_core_backend.core.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReadOnlyDTO {

    private Long id;
    private String username;  // περιέχει το email !!
    private String firstname;
    private String lastname;
    private LocalDate dateOfBirth;
    private GenderType gender;
    private Role role;
}
