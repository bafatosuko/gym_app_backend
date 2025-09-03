package gr.myproject.wod_core_backend.dto;

import gr.myproject.wod_core_backend.core.enums.SubscriptionType;
import gr.myproject.wod_core_backend.model.User;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionInsertDTO {

    // Δεν χρειαζεται Πλεον
//    @NotNull
//    private LocalDate startDate;

    @NotNull
    private SubscriptionType subscriptionType;


}
