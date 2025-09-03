package gr.myproject.wod_core_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "workout_session")
public class WorkoutSession extends AbstractEntity {


    private String title; // WOD, STRENGTH, INTRO

    private String description;

    private LocalTime startTime; // Ώρα έναρξης

    private Integer capacity;

    @Column(name = "session_date")
    private LocalDate sessionDate;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private User trainer;

    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Booking> bookings = new HashSet<>();

    public void addBooking(Booking booking) {
        if (bookings == null) bookings = new HashSet<>();
        bookings.add(booking);
        booking.setWorkoutSession(this);
    }



    // Επιστρέφει true αν ο συγκεκριμένος πελάτης έχει κάνει ενεργή κράτηση σε αυτό το session
    public boolean hasCustomerBooked(User customer) {
        return bookings.stream()
                .anyMatch(b -> !b.getIsCancelled() && b.getUser().getId().equals(customer.getId()));
    }

    // Επιστρέφει τις διαθέσιμες θέσεις (χωρητικότητα - κρατήσεις που δεν έχουν ακυρωθεί)
    public int getAvailableSlots() {
        long activeBookings = bookings.stream()
                .filter(b -> !b.getIsCancelled())
                .count();
        return Math.max(0, (capacity != null ? capacity : 0) - (int) activeBookings);
    }

    // Επιστρέφει true αν το session έχει ήδη περάσει (ημερομηνία + ώρα)
    public boolean isPast() {
        LocalDateTime sessionDateTime = LocalDateTime.of(sessionDate, startTime);
        return sessionDateTime.isBefore(LocalDateTime.now());
    }


    public WorkoutSession(String title, String description, LocalTime startTime,
                          Integer capacity, LocalDate sessionDate, User trainer) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.capacity = capacity;
        this.sessionDate = sessionDate;
        this.trainer = trainer;
    }

}
