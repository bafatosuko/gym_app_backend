package gr.myproject.wod_core_backend.repository;

import gr.myproject.wod_core_backend.dto.BookingReadOnlyDTO;
import gr.myproject.wod_core_backend.model.Booking;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.model.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> , JpaSpecificationExecutor<Booking> {

    List<Booking> findByUser(User user);

    List<Booking> findByWorkoutSession(WorkoutSession workoutSession);


    List<Booking> findAllByUser_IdOrderByWorkoutSession_SessionDateDesc(Long userId);

    Optional<Booking> findByUserAndWorkoutSession_SessionDateAndIsCancelledFalse(User user, LocalDate date);

    boolean existsByUserAndWorkoutSession_SessionDateAndIsCancelledFalse(User user, LocalDate sessionDate);

    void deleteAllByUser(User user);

    void deleteAllByWorkoutSessionId(Long sessionId);

    void deleteAllByWorkoutSession(WorkoutSession session);
}
