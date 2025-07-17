package gr.myproject.wod_core_backend.repository;

import gr.myproject.wod_core_backend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> , JpaSpecificationExecutor<Booking> {

    List<Booking> findAllByCustomerId(Long customerId);

    List<Booking> findAllByWorkoutSessionId(Long workoutSessionId);

    Optional<Booking> findById(Long id);
}
