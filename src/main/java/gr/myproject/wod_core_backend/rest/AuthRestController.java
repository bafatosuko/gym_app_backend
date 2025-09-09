package gr.myproject.wod_core_backend.rest;

import gr.myproject.wod_core_backend.authentication.AuthenticationService;
import gr.myproject.wod_core_backend.core.exceptions.AppObjectNotAuthorizedException;
import gr.myproject.wod_core_backend.dto.AuthenticationRequestDTO;
import gr.myproject.wod_core_backend.dto.AuthenticationResponseDTO;
import gr.myproject.wod_core_backend.dto.UserReadOnlyDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);

    //private final IUserService userService;
    private final AuthenticationService authenticationService;


    @Operation(
            summary = "Login",
            responses = {
                    @ApiResponse(responseCode = "200", description = "You logged in successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponseDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO)
            throws AppObjectNotAuthorizedException {
        AuthenticationResponseDTO authenticationResponseDTO = authenticationService.authenticate(authenticationRequestDTO);
        return new ResponseEntity<>(authenticationResponseDTO, HttpStatus.OK);
    }
}
