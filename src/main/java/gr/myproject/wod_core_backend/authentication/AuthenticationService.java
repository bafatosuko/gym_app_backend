package gr.myproject.wod_core_backend.authentication;


import gr.myproject.wod_core_backend.core.exceptions.AppObjectNotAuthorizedException;
import gr.myproject.wod_core_backend.dto.AuthenticationRequestDTO;
import gr.myproject.wod_core_backend.dto.AuthenticationResponseDTO;
import gr.myproject.wod_core_backend.model.User;
import gr.myproject.wod_core_backend.repository.UserRepository;
import gr.myproject.wod_core_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;


    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto)
            throws AppObjectNotAuthorizedException {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppObjectNotAuthorizedException("User", "User not authorized"));

        String token = jwtService.generateToken(authentication.getName(), user.getRole().name());
        return new AuthenticationResponseDTO(user.getFirstname(), user.getLastname(), token, user.getRole());
    }
}
