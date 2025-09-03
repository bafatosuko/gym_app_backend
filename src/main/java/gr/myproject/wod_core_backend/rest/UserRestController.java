package gr.myproject.wod_core_backend.rest;

import gr.myproject.wod_core_backend.core.enums.Role;
import gr.myproject.wod_core_backend.core.exceptions.*;
import gr.myproject.wod_core_backend.dto.UserInsertDTO;
import gr.myproject.wod_core_backend.dto.UserReadOnlyDTO;

import gr.myproject.wod_core_backend.service.UserService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;


    @Operation(
            summary = "Create a new user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserReadOnlyDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input"),
                    @ApiResponse(responseCode = "409", description = "User already exists")
            }
    )
    @PostMapping("/save")
    public ResponseEntity<UserReadOnlyDTO> createUser(
            @Valid @RequestBody UserInsertDTO userInsertDTO
    ) throws AppObjectAlreadyExists, AppObjectInvalidArgumentException, IOException {
        UserReadOnlyDTO createdUser = userService.saveUser(userInsertDTO);
        return ResponseEntity.ok(createdUser);
    }

    @Operation(summary = "Get paginated and sorted users")
    @GetMapping
    public ResponseEntity<Page<UserReadOnlyDTO>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) {
        return ResponseEntity.ok(userService.getPaginatedSortedUsers(page, size, sortBy, sortDirection));
    }



    @Operation(summary = "Get all users")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/trainer/all")
    public ResponseEntity<List<UserReadOnlyDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Delete user by id")
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/trainer/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws AppObjectNotFoundException, AppObjectInvalidArgumentException {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get users by role")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/trainer/role/{role}")
    public ResponseEntity<List<UserReadOnlyDTO>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(userService.findUsersByRole(role));
    }

    // Για το swap απο Customer σε Trainer !!
    @Operation(summary = "Promote a Customer to Trainer(only Trainers and Admin)")
    @PutMapping("/trainer/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'TRAINER')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserReadOnlyDTO> promoteToTrainer(@PathVariable Long userId)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        UserReadOnlyDTO updatedUser = userService.promoteCustomerToTrainer(userId, currentUsername);
        return ResponseEntity.ok(updatedUser);
    }

}
