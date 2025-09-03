package gr.myproject.wod_core_backend.dto;


import gr.myproject.wod_core_backend.core.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDTO {

    private String firstname;
    private String lastname;
    private String token;
    private Role role;
}
