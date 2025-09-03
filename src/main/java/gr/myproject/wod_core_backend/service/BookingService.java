package gr.myproject.wod_core_backend.service;

import gr.myproject.wod_core_backend.core.exceptions.AppGenericException;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectAlreadyExists;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectInvalidArgumentException;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectNotFoundException;

import gr.myproject.wod_core_backend.dto.BookingReadOnlyDTO;
import gr.myproject.wod_core_backend.mapper.BookingMapper;
import gr.myproject.wod_core_backend.mapper.WorkoutSessionMapper;
import gr.myproject.wod_core_backend.model.Booking;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.model.WorkoutSession;
import gr.myproject.wod_core_backend.repository.BookingRepository;
import gr.myproject.wod_core_backend.repository.UserRepository;
import gr.myproject.wod_core_backend.repository.WorkoutSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final WorkoutSessionRepository workoutSessionRepository;
    private final UserRepository userRepository;



    private final BookingMapper bookingMapper;
    private final WorkoutSessionMapper workoutSessionMapper;

    @Transactional
    public BookingReadOnlyDTO createBooking(Long userId, Long sessionId)
    throws AppObjectNotFoundException,  AppObjectAlreadyExists{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));

        WorkoutSession session = workoutSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppObjectNotFoundException("WorkoutSession" , "Workout session not found"));

        //  Έλεγχος για duplicate booking την ίδια μέρα
        boolean alreadyBooked = bookingRepository
                .existsByUserAndWorkoutSession_SessionDateAndIsCancelledFalse(user, session.getSessionDate());

        if (alreadyBooked) {
            throw new AppObjectAlreadyExists("Booking", "You already did a booking today! Only one booking per day is  allowed");
        }

        // Δημιουργία νέας κράτησης
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setWorkoutSession(session);
        booking.setBookingTime(LocalDateTime.now());
        booking.setIsCancelled(false);

        bookingRepository.save(booking);


        return bookingMapper.mapToBookingReadOnlyDTO(booking);
    }


    // Ακύρωση booking
    @Transactional
    public BookingReadOnlyDTO cancelBooking(Long bookingId)
            throws AppGenericException, AppObjectNotFoundException {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new AppObjectNotFoundException("Booking", "Booking not found"));

        LocalDateTime sessionDateTime = LocalDateTime.of(
                booking.getWorkoutSession().getSessionDate(),
                booking.getWorkoutSession().getStartTime()
        );

        if (Duration.between(LocalDateTime.now(), sessionDateTime).toHours() < 1) {
            throw new AppGenericException("Booking", "Cannot cancel booking less than 1 hour before session");
        }

        booking.setIsCancelled(true);
        bookingRepository.save(booking);

        return bookingMapper.mapToBookingReadOnlyDTO(booking);
    }

    // Λήψη όλων των bookings ενός χρήστη
    public List<BookingReadOnlyDTO> getBookingsForUser(Long userId) throws AppObjectNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));

        return bookingRepository.findAllByUser_IdOrderByWorkoutSession_SessionDateDesc(userId)
                .stream()
                .map(bookingMapper::mapToBookingReadOnlyDTO)
                .toList();
    }

    // Λήψη όλων των bookings ενός session
    public List<Booking> getBookingsForSession(Long sessionId) throws AppObjectNotFoundException{
        WorkoutSession session = workoutSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppObjectNotFoundException("Session", "Workout session not found"));
        return bookingRepository.findByWorkoutSession(session);
    }
}

