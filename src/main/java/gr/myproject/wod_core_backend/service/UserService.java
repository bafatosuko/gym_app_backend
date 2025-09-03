package gr.myproject.wod_core_backend.service;

import gr.myproject.wod_core_backend.core.enums.Role;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectAlreadyExists;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectInvalidArgumentException;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectNotFoundException;

import gr.myproject.wod_core_backend.dto.UserInsertDTO;
import gr.myproject.wod_core_backend.dto.UserReadOnlyDTO;
import gr.myproject.wod_core_backend.mapper.UserMapper;
import gr.myproject.wod_core_backend.model.Subscription;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.model.WorkoutSession;
import gr.myproject.wod_core_backend.repository.BookingRepository;
import gr.myproject.wod_core_backend.repository.UserRepository;
import gr.myproject.wod_core_backend.repository.WorkoutSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserMapper mapper;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final WorkoutSessionRepository workoutSessionRepository;


    // -------------------- CREATE --------------------
    @Transactional(rollbackOn = {AppObjectAlreadyExists.class, AppObjectInvalidArgumentException.class})
    public UserReadOnlyDTO saveUser(UserInsertDTO dto)
            throws AppObjectAlreadyExists, AppObjectInvalidArgumentException {

        String username = dto.getUsername();

        if (userRepository.existsByUsername(username)) {
            LOGGER.warn("User with username {} already exists", username);
            throw new AppObjectAlreadyExists("User", "User with username " + username + " already exists");
        }

        try {
            User user = mapper.mapToUserEntity(dto);
            user.setIsActive(true); // default ενεργός
            User saved = userRepository.save(user);
            LOGGER.info("User {} successfully created", username);
            return mapper.mapToUserReadOnlyDTO(saved);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Invalid user data for {}: {}", username, e.getMessage());
            throw new AppObjectInvalidArgumentException("User", "Invalid user data");
        }
    }

    // -------------------- READ ALL --------------------
    public List<UserReadOnlyDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(mapper::mapToUserReadOnlyDTO)
                .collect(Collectors.toList());
    }


    public UserReadOnlyDTO getUserById(Long id) throws AppObjectNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with id " + id + " not found"));
        return mapper.mapToUserReadOnlyDTO(user);
    }


    @Transactional
    public void deleteUserById(Long id) throws AppObjectNotFoundException , AppObjectInvalidArgumentException{
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppObjectNotFoundException("User", "User with id " + id + " not found"));

        if (user.getRole() == Role.ADMIN ) throw new AppObjectInvalidArgumentException("Admin", "You cant delete ADMIN");

        // οταν σβηνει ενα trainer θα σβηνει και τα workoutSession του
        if (user.getRole() == Role.TRAINER) {
            List<WorkoutSession> sessions = workoutSessionRepository.findAllByTrainer(user);
            for (WorkoutSession session : sessions) {
                // σβηνω πρωτα τα bookings
                bookingRepository.deleteAllByWorkoutSession(session);

                // Μετά τα sessions
                workoutSessionRepository.delete(session);
            }
        }



        bookingRepository.deleteAllByUser(user);
        userRepository.delete(user);
        LOGGER.info("User with id {} deleted", id);
    }





//    public Optional<UserReadOnlyDTO> findByUsername(String username) {
//        return userRepository.findByUsername(username)
//                .map(mapper::mapToUserReadOnlyDTO);
//    }

    // -------------------- PAGINATION & SORT --------------------
    @Transactional
    public Page<UserReadOnlyDTO> getPaginatedSortedUsers(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable)
                .map(mapper::mapToUserReadOnlyDTO);
    }

    // -------------------- FILTER BY ROLE --------------------
    public List<UserReadOnlyDTO> findUsersByRole(Role role) {
        return userRepository.findAllByRole(role)
                .stream()
                .map(mapper::mapToUserReadOnlyDTO)
                .collect(Collectors.toList());
    }





     // Μονο ο ADMIN,TRAINER μπορουν να αλλαξουν το ρολο απο εναν χρηστη
     @Transactional
     public UserReadOnlyDTO promoteCustomerToTrainer(Long userId, String currentUsername)
             throws AppObjectNotFoundException, AppObjectInvalidArgumentException {


         User currentUser = userRepository.findByUsername(currentUsername)
                 .orElseThrow(() -> new AppObjectNotFoundException("User", "Current user not found"));

         // Ελέγχουμε αν είναι ADMIN ή TRAINER
         if (currentUser.getRole() != Role.ADMIN && currentUser.getRole() != Role.TRAINER) {
             throw new AppObjectInvalidArgumentException("User", "Only ADMIN or TRAINER can change roles to TRAINER");
         }

         // Τσεκαρουμε τον χρήστη
         User user = userRepository.findById(userId)
                 .orElseThrow(() -> new AppObjectNotFoundException("User", "User with id " + userId + " not found"));

         //  ADMIN
         if (user.getRole() == Role.ADMIN) {
             throw new AppObjectInvalidArgumentException("User", "Cannot change ADMIN role");
         }

         // Επιτρέπουμε μόνο αλλαγή από CUSTOMER → TRAINER
         if (user.getRole() != Role.CUSTOMER) {
             throw new AppObjectInvalidArgumentException("User", "Only CUSTOMER can be promoted to TRAINER");
         }

         // Κάνε αλλαγή σε TRAINER
         user.setRole(Role.TRAINER);

         User saved = userRepository.save(user);
         return mapper.mapToUserReadOnlyDTO(saved);
     }

}
