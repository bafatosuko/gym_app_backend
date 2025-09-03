package gr.myproject.wod_core_backend.model;

import gr.myproject.wod_core_backend.core.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "subscriptions")
public class Subscription extends AbstractEntity {




    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_type")
    private SubscriptionType subscriptionType;

    @Column(name = "active_subscription")
    private Boolean activeSubscription;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;



}
