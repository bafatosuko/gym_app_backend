package gr.myproject.wod_core_backend.service;

import gr.myproject.wod_core_backend.core.enums.Role;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectInvalidArgumentException;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectNotFoundException;
import gr.myproject.wod_core_backend.dto.WorkoutSessionCalendarDTO;
import gr.myproject.wod_core_backend.dto.WorkoutSessionReadOnlyDTO;
import gr.myproject.wod_core_backend.mapper.WorkoutSessionMapper;
import gr.myproject.wod_core_backend.model.Booking;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.model.WorkoutSession;
import gr.myproject.wod_core_backend.repository.BookingRepository;
import gr.myproject.wod_core_backend.repository.UserRepository;
import gr.myproject.wod_core_backend.repository.WorkoutSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutSessionMapper workoutSessionMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Transactional
    public WorkoutSession createSession(WorkoutSession session, Long userId)
    throws AppObjectNotFoundException, AppObjectInvalidArgumentException{

        User trainer = userRepository.findById(userId)
                .orElseThrow(() -> new AppObjectNotFoundException("Trainer", "Trainer not found"));

        if(trainer.getRole() == Role.CUSTOMER)
            throw new AppObjectInvalidArgumentException("Customer", "You  don't have Permission tou do that as a Customer");

        session.setTrainer(trainer);


        return workoutSessionRepository.save(session);
    }

    @Transactional
    public WorkoutSession updateSession(Long sessionId, WorkoutSession updatedSession, Long trainerId)
            throws AppObjectNotFoundException , AppObjectInvalidArgumentException{

        WorkoutSession existing = workoutSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppObjectNotFoundException("WorkoutSession", "Not found"));

        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "Trainer not found"));

        if(trainer.getRole() == Role.CUSTOMER)
            throw new AppObjectInvalidArgumentException("Customer", "You  don't have Permission tou do that as a Customer");

        // Assign trainer ξανά για να μην χαθεί
        existing.setTrainer(trainer);

        // Update fields
        existing.setTitle(updatedSession.getTitle());
        existing.setDescription(updatedSession.getDescription());
        existing.setSessionDate(updatedSession.getSessionDate());
        existing.setStartTime(updatedSession.getStartTime());
        existing.setCapacity(updatedSession.getCapacity());

        return workoutSessionRepository.save(existing);
    }


    @Transactional
    public void deleteSession(Long sessionId, Long trainerId) throws AppObjectNotFoundException,
            AppObjectInvalidArgumentException{
        WorkoutSession session = workoutSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppObjectNotFoundException("WorkoutSession", "Not found"));

        // Optional: check αν ο trainer είναι ίδιος με τον creator
//        if (!session.getTrainer().getId().equals(trainerId)) {
//            throw new SecurityException("You are not allowed to delete this session");
//        }



        User trainer = userRepository.findById(trainerId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "Trainer not found"));

        if(trainer.getRole() == Role.CUSTOMER)
            throw new AppObjectInvalidArgumentException("Customer", "You  don't have Permission tou do that as a Customer");

        // Διαγραφουμε πρωτα τα booking που εχει το session και μετα το session

        //bookingRepository.deleteAllByWorkoutSessionId(sessionId);

        workoutSessionRepository.delete(session);
    }

    public WorkoutSession getSession(Long id)
    throws AppObjectNotFoundException{
        return workoutSessionRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("WorkoutSession", "WorkoutSession not found"));
    }

    public List<WorkoutSessionReadOnlyDTO> getAllSessions() {
        List<WorkoutSession> sessions = workoutSessionRepository.findAll();

        return sessions.stream()
                .map(workoutSessionMapper::mapToWorkoutSessionReadOnlyDTO)
                .collect(Collectors.toList());
    }

//    public List<WorkoutSessionReadOnlyDTO> getSessionsByDate(LocalDate date) {
//        List<WorkoutSession> sessions = workoutSessionRepository.findBySessionDateOrderByStartTimeAsc(date);
//        return sessions.stream()
//                .map(workoutSessionMapper::mapToWorkoutSessionReadOnlyDTO)
//                .collect(Collectors.toList());
//    }


    public List<WorkoutSessionCalendarDTO> getSessionsWithUserStatus(LocalDate date, Long userId)
    throws  AppObjectNotFoundException{
        List<WorkoutSession> sessions = workoutSessionRepository.findBySessionDateOrderByStartTimeAsc(date);

        // Παίρνουμε το ενεργό booking του χρήστη εκείνη τη μέρα, αν υπάρχει
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User not found"));
        Booking userBookingForDay = bookingRepository
                .findByUserAndWorkoutSession_SessionDateAndIsCancelledFalse(user, date)
                .orElse(null);

        return sessions.stream()
                .map(session -> workoutSessionMapper.mapCalendarDTO(session, userBookingForDay))
                .collect(Collectors.toList());
    }
}

