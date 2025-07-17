package gr.myproject.wod_core_backend.dto;

import java.time.LocalDateTime;

public class BookingReadOnlyDTO {

    private Long id;
    private LocalDateTime bookingTime;
    private Boolean isCancelled;
    private CustomerReadOnlyDTO customer;
    private WorkoutSessionReadOnlyDTO workoutSession;
}
