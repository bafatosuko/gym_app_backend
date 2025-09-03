package gr.myproject.wod_core_backend.rest;


import gr.myproject.wod_core_backend.core.exceptions.AppObjectInvalidArgumentException;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectNotFoundException;
import gr.myproject.wod_core_backend.dto.SubscriptionInsertDTO;
import gr.myproject.wod_core_backend.dto.SubscriptionReadOnlyDTO;
import gr.myproject.wod_core_backend.service.SubscriptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class SubscriptionRestController {

    private final SubscriptionService subscriptionService;

    @Operation(
            summary = "Create subscription for current logged-in user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Subscription created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = SubscriptionReadOnlyDTO.class))),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping
    public ResponseEntity<SubscriptionReadOnlyDTO> createSubscription(@Valid @RequestBody SubscriptionInsertDTO dto)
            throws AppObjectNotFoundException, AppObjectInvalidArgumentException {

        SubscriptionReadOnlyDTO created = subscriptionService.createSubscriptionForCurrentUser(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Get subscription of current user")
    @GetMapping("/me")
    public ResponseEntity<List<SubscriptionReadOnlyDTO>> getMySubscription() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<SubscriptionReadOnlyDTO> subscriptions = subscriptionService.getAllSubscriptions(username);
        if (subscriptions.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(subscriptions);
    }

    @Operation(summary = "Check if current user has active subscription")
    @GetMapping("/me/active")
    public ResponseEntity<Boolean> hasActiveSubscription() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean active = subscriptionService.isActive(username);
        return ResponseEntity.ok(active);
    }
}