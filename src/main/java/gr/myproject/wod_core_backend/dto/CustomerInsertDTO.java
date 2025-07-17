package gr.myproject.wod_core_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerInsertDTO {

    @NotNull(message = "isActive field is required")
    private Boolean isActive;

    @NotNull(message = "isSubscribed field is required")
    private Boolean isSubscribed;

    @NotNull(message = "User details is required")
    private UserInsertDTO user;
}
