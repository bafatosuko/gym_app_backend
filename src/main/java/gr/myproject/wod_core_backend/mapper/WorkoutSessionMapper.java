package gr.myproject.wod_core_backend.mapper;

import gr.myproject.wod_core_backend.dto.WorkoutSessionCalendarDTO;
import gr.myproject.wod_core_backend.dto.WorkoutSessionInsertDTO;
import gr.myproject.wod_core_backend.dto.WorkoutSessionReadOnlyDTO;
import gr.myproject.wod_core_backend.model.Booking;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.model.WorkoutSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Component
public class WorkoutSessionMapper {

    private final UserMapper userMapper;

    public WorkoutSession mapToWorkoutSessionEntity(WorkoutSessionInsertDTO dto, User trainer) {
        WorkoutSession session = new WorkoutSession();
        session.setTitle(dto.getTitle());
        session.setDescription(dto.getDescription());
        session.setStartTime(dto.getStartTime());
        session.setSessionDate(dto.getSessionDate());
        session.setCapacity(dto.getCapacity());
        session.setTrainer(trainer);
        return session;
    }

    public WorkoutSessionReadOnlyDTO mapToWorkoutSessionReadOnlyDTO(WorkoutSession session) {
        WorkoutSessionReadOnlyDTO dto = new WorkoutSessionReadOnlyDTO();
        dto.setId(session.getId());
        dto.setTitle(session.getTitle());
        dto.setDescription(session.getDescription());
        dto.setStartTime(session.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        dto.setSessionDate(session.getSessionDate());
        dto.setCapacity(session.getCapacity());
        dto.setAvailableSlots(session.getAvailableSlots());
        dto.setTrainer(userMapper.mapToUserReadOnlyDTO(session.getTrainer()));
        return dto;
    }

    public WorkoutSessionCalendarDTO mapCalendarDTO(WorkoutSession session, Booking userBookingForDay) {
        WorkoutSessionCalendarDTO dto = new WorkoutSessionCalendarDTO();
        dto.setId(session.getId());
        dto.setTitle(session.getTitle());
        dto.setDescription(session.getDescription());
        dto.setStartTime(session.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        dto.setSessionDate(session.getSessionDate());
        dto.setCapacity(session.getCapacity());
        dto.setAvailableSlots(session.getAvailableSlots());
        dto.setPast(session.isPast());
        dto.setTrainer(userMapper.mapToUserReadOnlyDTO(session.getTrainer()));

        // Αν υπάρχει booking για τον χρήστη εκείνη τη μέρα
        if (userBookingForDay != null && !userBookingForDay.getIsCancelled()) {
            // Αν είναι το ίδιο session, το αφήνουμε ενεργό για Cancel Check-In
            if (userBookingForDay.getWorkoutSession().getId().equals(session.getId())) {
                dto.setBookingId(userBookingForDay.getId());
                dto.setDisabled(false);
            } else {
                // Άλλα sessions της ίδιας ημέρας είναι disabled
                dto.setBookingId(null);
                dto.setDisabled(true);
            }
        } else {
            // Δεν υπάρχει booking -> μπορεί να κάνει check-in
            dto.setBookingId(null);
            dto.setDisabled(false);
        }



        return dto;
    }
}
