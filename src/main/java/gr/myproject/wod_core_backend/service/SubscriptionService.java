package gr.myproject.wod_core_backend.service;


import gr.myproject.wod_core_backend.core.exceptions.AppObjectInvalidArgumentException;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectNotFoundException;
import gr.myproject.wod_core_backend.dto.SubscriptionInsertDTO;
import gr.myproject.wod_core_backend.dto.SubscriptionReadOnlyDTO;
import gr.myproject.wod_core_backend.mapper.SubscriptionMapper;
import gr.myproject.wod_core_backend.model.Subscription;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.repository.SubscriptionRepository;
import gr.myproject.wod_core_backend.repository.UserRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SubscriptionService {



    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final SubscriptionMapper mapper;



    // ---- Δημιουργία συνδρομής για τον συνδεδεμένο χρήστη ----
    public SubscriptionReadOnlyDTO createSubscriptionForCurrentUser(SubscriptionInsertDTO dto)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));

        LocalDate startDate;

        Optional<Subscription> lastSubscription = user.getSubscriptions()
                .stream()
                .max(Comparator.comparing(Subscription::getEndDate));

        if (lastSubscription.isPresent() && lastSubscription.get().getEndDate().isAfter(LocalDate.now())) {
            // Αν η τελευταία συνδρομή δεν έχει λήξει ακόμα, η νέα ξεκινά μετά το τέλος της
            startDate = lastSubscription.get().getEndDate().plusDays(1);
        } else {
            // Αλλιώς ξεκινάει σήμερα
            startDate = LocalDate.now();
        }

        // Υπολογισμός endDate με βάση τον τύπο συνδρομής
        LocalDate endDate;
        switch (dto.getSubscriptionType()) {
            case MONTHLY -> endDate = startDate.plusMonths(1);
            case WEEKLY -> endDate = startDate.plusWeeks(1);
            case ONE_TIME -> endDate = startDate.plusDays(1);
            case DAY_PASS -> endDate = startDate;
            default -> throw new AppObjectInvalidArgumentException("Subscription", "Unknown subscription type");
        }
        Subscription subscription = mapper.mapToSubscriptionEntity(dto, user);
        subscription.setStartDate(startDate);
        subscription.setEndDate(endDate);
        subscription.setActiveSubscription(true);

        Subscription saved = subscriptionRepository.save(subscription);
        return mapper.mapToSubscriptionReadOnlyDTO(saved);
    }

    // --- Προβολή όλων των συνδρομών του χρήστη ----
    public List<SubscriptionReadOnlyDTO> getAllSubscriptions(String username) {
        return subscriptionRepository.findAllByUserUsernameOrderByStartDateDesc(username)
                .stream()
                .map(mapper::mapToSubscriptionReadOnlyDTO)
                .toList();
    }

    // ---- Έλεγχος ενεργής συνδρομής (σύμφωνα με ημερομηνίες) ----
    public boolean isActive(String username) {
        LocalDate today = LocalDate.now();
        return subscriptionRepository.existsByUserUsernameAndActiveSubscriptionTrueAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                username, today, today
        );
    }

}
