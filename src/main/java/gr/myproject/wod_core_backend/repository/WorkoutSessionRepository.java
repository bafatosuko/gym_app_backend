package gr.myproject.wod_core_backend.repository;

import gr.myproject.wod_core_backend.model.Subscription;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.model.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> , JpaSpecificationExecutor<WorkoutSession> {



    List<WorkoutSession> findBySessionDateOrderByStartTimeAsc(LocalDate sessionDate);



    List<WorkoutSession> findAllByTrainer(User user);
}
