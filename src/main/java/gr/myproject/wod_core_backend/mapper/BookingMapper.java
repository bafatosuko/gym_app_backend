package gr.myproject.wod_core_backend.mapper;

import gr.myproject.wod_core_backend.dto.BookingInsertDTO;
import gr.myproject.wod_core_backend.dto.BookingReadOnlyDTO;
import gr.myproject.wod_core_backend.model.Booking;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.model.WorkoutSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookingMapper {

    private final UserMapper userMapper;
    private final WorkoutSessionMapper workoutSessionMapper;



    public Booking mapToBookingEntity(BookingInsertDTO dto, User user, WorkoutSession session) {
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setWorkoutSession(session);
        return booking;
    }




    public BookingReadOnlyDTO mapToBookingReadOnlyDTO(Booking booking) {
        return new BookingReadOnlyDTO(
                booking.getId(),
                booking.getBookingTime(),
                booking.getIsCancelled(),
                userMapper.mapToUserReadOnlyDTO(booking.getUser()),
                workoutSessionMapper.mapToWorkoutSessionReadOnlyDTO(booking.getWorkoutSession())
        );
    }
}
