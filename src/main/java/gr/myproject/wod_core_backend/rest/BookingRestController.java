package gr.myproject.wod_core_backend.rest;


import gr.myproject.wod_core_backend.core.exceptions.AppObjectNotFoundException;
import gr.myproject.wod_core_backend.dto.BookingInsertDTO;
import gr.myproject.wod_core_backend.dto.BookingReadOnlyDTO;
import gr.myproject.wod_core_backend.model.Booking;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class BookingRestController {

    private final BookingService bookingService;

    @Operation(summary = "Δημιουργία νέου booking")
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody Map<String, Long> body, Authentication auth) {
        try {
            User user = (User) auth.getPrincipal(); // Ο authenticated χρήστης
            Long sessionId = body.get("workoutSessionId");

            BookingReadOnlyDTO bookingDTO = bookingService.createBooking(user.getId(), sessionId);
            return ResponseEntity.ok(bookingDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "Ακύρωση booking")
    @PostMapping("/cancel/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        try {
            BookingReadOnlyDTO dto = bookingService.cancelBooking(id);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @Operation(summary = "Λήψη bookings συγκεκριμένου χρήστη με userId")
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Booking>> getUserBookings(@PathVariable Long userId)
//            throws AppObjectNotFoundException {
//        return ResponseEntity.ok(bookingService.getBookingsForUser(userId));
//    }

    @Operation(summary = "Λήψη bookings συγκεκριμένου workout session")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<Booking>> getSessionBookings(@PathVariable Long sessionId)
            throws AppObjectNotFoundException {
        return ResponseEntity.ok(bookingService.getBookingsForSession(sessionId));
    }

    @Operation(summary = "Λήψη των δικών μου bookings (από JWT)")
    @GetMapping("/my")
    public ResponseEntity<List<BookingReadOnlyDTO>> getMyBookings(Authentication authentication)
            throws AppObjectNotFoundException {

        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(bookingService.getBookingsForUser(user.getId()));
    }
}
