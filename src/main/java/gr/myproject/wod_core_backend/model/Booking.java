package gr.myproject.wod_core_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "bookings")
public class Booking extends AbstractEntity {


    private LocalDateTime bookingTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // Ο πελάτης που κάνει την κράτηση

    @Column(name = "is_cancelled")
    private Boolean isCancelled;

    // todo  private id CancelledBy

    @ManyToOne
    @JoinColumn(name = "workout_session_id")
    private WorkoutSession workoutSession;


}
