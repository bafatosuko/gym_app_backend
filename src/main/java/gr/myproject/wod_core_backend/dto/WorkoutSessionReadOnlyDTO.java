package gr.myproject.wod_core_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkoutSessionReadOnlyDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDate sessionDate;

    // Στέλνω string για να μπορω να το διαχειριστω καλύτερα στο front end
   // private LocalTime startTime;
    private String startTime;
    private Integer capacity;
    private int availableSlots;
    private boolean isPast;
    private UserReadOnlyDTO trainer;


}
