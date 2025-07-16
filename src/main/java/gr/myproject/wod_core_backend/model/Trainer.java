package gr.myproject.wod_core_backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "trainers")
public class Trainer extends AbstractEntity {


    @Column(name = "is_active")
    private Boolean isActive;



}
