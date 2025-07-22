package gr.myproject.wod_core_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerReadOnlyDTO {

    private Long id;
    private String uuid;
    private Boolean isActive;
    private Boolean isSubscribed;
    private UserReadOnlyDTO user;
}
