package gr.myproject.wod_core_backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customers")
public class Customer extends AbstractEntity {




    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "is_subscribed")
    private Boolean isSubscribed;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;



}
