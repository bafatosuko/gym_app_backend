package gr.myproject.wod_core_backend.rest;

import gr.myproject.wod_core_backend.core.enums.Role;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectInvalidArgumentException;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectNotFoundException;
import gr.myproject.wod_core_backend.dto.WorkoutSessionCalendarDTO;
import gr.myproject.wod_core_backend.dto.WorkoutSessionInsertDTO;
import gr.myproject.wod_core_backend.dto.WorkoutSessionReadOnlyDTO;
import gr.myproject.wod_core_backend.mapper.WorkoutSessionMapper;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.model.WorkoutSession;
import gr.myproject.wod_core_backend.service.WorkoutSessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workout-sessions")
@RequiredArgsConstructor
@Tag(name = "Workout Sessions", description = "CRUD για Workout Sessions")
@SecurityRequirement(name = "bearerAuth")
public class WorkoutSessionRestController {

    private final WorkoutSessionService workoutSessionService;
    private final WorkoutSessionMapper workoutSessionMapper;

    @Operation(summary = "Create a new workout session")
    @ApiResponse(responseCode = "200", description = "Workout session created")
    @ApiResponse(responseCode = "400", description = "Not authorized")
    @PostMapping
    public ResponseEntity<WorkoutSessionReadOnlyDTO> create(@RequestBody WorkoutSessionInsertDTO session, Authentication auth)
    throws AppObjectNotFoundException, AppObjectInvalidArgumentException{
        User user = (User) auth.getPrincipal();
        WorkoutSession created = workoutSessionService.createSession(session, user.getId());
        WorkoutSessionReadOnlyDTO dto = workoutSessionMapper.mapToWorkoutSessionReadOnlyDTO(created);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Update an existing workout session")
    @ApiResponse(responseCode = "200", description = "Workout session updated")
    @PutMapping("/{id}")
    public ResponseEntity<WorkoutSessionReadOnlyDTO> update(
            @PathVariable Long id,
            @RequestBody WorkoutSessionInsertDTO session,
            Authentication authentication
    ) throws AppObjectNotFoundException , AppObjectInvalidArgumentException{
        User trainer = (User) authentication.getPrincipal();
        WorkoutSession updated = workoutSessionService.updateSession(id, session, trainer.getId());
        return ResponseEntity.ok(workoutSessionMapper.mapToWorkoutSessionReadOnlyDTO(updated));
    }

    @Operation(summary = "Delete a workout session")
    @ApiResponse(responseCode = "200", description = "Workout session deleted")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            Authentication authentication
    ) throws AppObjectNotFoundException , AppObjectInvalidArgumentException {
        User trainer = (User) authentication.getPrincipal();
        workoutSessionService.deleteSession(id, trainer.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a workout session by ID")
    @ApiResponse(responseCode = "200", description = "Workout session found")
    @GetMapping("/{id}")
    public ResponseEntity<WorkoutSessionReadOnlyDTO> getById(@PathVariable Long id)
    throws AppObjectNotFoundException{
        WorkoutSession session = workoutSessionService.getSession(id);

        WorkoutSessionReadOnlyDTO dto = workoutSessionMapper.mapToWorkoutSessionReadOnlyDTO(session);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Get all workout sessions")
    @ApiResponse(responseCode = "200", description = "List of workout sessions")
    @GetMapping("/all")
    public ResponseEntity<List<WorkoutSessionReadOnlyDTO>> getAllSessions() {
        List<WorkoutSessionReadOnlyDTO> sessions = workoutSessionService.getAllSessions();
        return ResponseEntity.ok(sessions);
    }


    @Operation(summary = "Get workout sessions of specific day")
    @ApiResponse(responseCode = "200", description = "You got the list of this specific date!")
    @GetMapping
    public ResponseEntity<List<WorkoutSessionCalendarDTO>> getSessionsByDate(
            @RequestParam("date") String dateStr,
            Authentication authentication)
    throws AppObjectNotFoundException{

        LocalDate date = LocalDate.parse(dateStr); // yyyy-MM-dd format

        // Παίρνουμε τον authenticated user
        User user = (User) authentication.getPrincipal();

        // Καλούμε το service για να πάρουμε sessions + booking info + isDisabled
        List<WorkoutSessionCalendarDTO> sessionsWithStatus =
                workoutSessionService.getSessionsWithUserStatus(date, user.getId());

        return ResponseEntity.ok(sessionsWithStatus);
    }
}
