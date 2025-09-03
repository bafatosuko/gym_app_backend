package gr.myproject.wod_core_backend.dto;

import gr.myproject.wod_core_backend.core.enums.SubscriptionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionReadOnlyDTO {

    private Long id;

    private UserReadOnlyDTO user;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Active subscription is required field")
    private boolean activeSubscription;

    private SubscriptionType subscriptionType;

    @NotNull(message = "User ID is required")
    private Long userId;

}
