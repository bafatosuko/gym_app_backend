package gr.myproject.wod_core_backend.repository;

import gr.myproject.wod_core_backend.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByUserUsernameOrderByStartDateDesc(String username);

    boolean existsByUserUsernameAndActiveSubscriptionTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String username, LocalDate startDate, LocalDate endDate);
}