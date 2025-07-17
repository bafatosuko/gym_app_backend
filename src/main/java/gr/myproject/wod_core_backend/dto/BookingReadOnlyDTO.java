package gr.myproject.wod_core_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingReadOnlyDTO {

    private Long id;
    private LocalDateTime bookingTime;
    private Boolean isCancelled;
    private CustomerReadOnlyDTO customer;
    private WorkoutSessionReadOnlyDTO workoutSession;
}
