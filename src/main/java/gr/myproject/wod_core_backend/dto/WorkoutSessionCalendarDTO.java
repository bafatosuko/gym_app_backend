package gr.myproject.wod_core_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WorkoutSessionCalendarDTO extends WorkoutSessionReadOnlyDTO {

    private Long bookingId;
    private boolean isDisabled;
}
