package gr.myproject.wod_core_backend.mapper;



import gr.myproject.wod_core_backend.dto.SubscriptionInsertDTO;
import gr.myproject.wod_core_backend.dto.SubscriptionReadOnlyDTO;
import gr.myproject.wod_core_backend.model.Subscription;
import gr.myproject.wod_core_backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {



    public Subscription mapToSubscriptionEntity(SubscriptionInsertDTO dto, User user) {
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(dto.getSubscriptionType());
        subscription.setActiveSubscription(true);
        subscription.setUser(user);
        return subscription;
    }

    public SubscriptionReadOnlyDTO mapToSubscriptionReadOnlyDTO(Subscription subscription) {
        SubscriptionReadOnlyDTO dto = new SubscriptionReadOnlyDTO();
        dto.setId(subscription.getId());
        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setSubscriptionType(subscription.getSubscriptionType());
        dto.setUserId(subscription.getUser().getId());

        // Υπολογισμός ενεργής συνδρομής με βάση τις ημερομηνίες
        LocalDate today = LocalDate.now();
        boolean isActive = !today.isBefore(subscription.getStartDate()) &&
                !today.isAfter(subscription.getEndDate());
        dto.setActiveSubscription(isActive);

        return dto;
    }

}
