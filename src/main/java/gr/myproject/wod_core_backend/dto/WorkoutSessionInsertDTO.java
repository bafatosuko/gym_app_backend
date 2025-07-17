package gr.myproject.wod_core_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class WorkoutSessionInsertDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "Session date is required")
    private LocalDate sessionDate;

    @NotNull(message = "Capacity is required")
    private Integer capacity;

    @NotNull(message = "Trainer ID is required")
    private Long trainerId;
}
