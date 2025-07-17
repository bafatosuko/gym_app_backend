package gr.myproject.wod_core_backend.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class WorkoutSessionReadOnlyDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private Integer capacity;
    private int availableSlots;
    private boolean isPast;
    private TrainerReadOnlyDTO trainer;
}
