package gr.myproject.wod_core_backend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainerReadOnlyDTO {
    private Long id;
    private String uuid;
    private UserReadOnlyDTO user;

}
