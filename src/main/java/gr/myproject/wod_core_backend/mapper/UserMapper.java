package gr.myproject.wod_core_backend.mapper;

import gr.myproject.wod_core_backend.core.enums.Role;
import gr.myproject.wod_core_backend.dto.SubscriptionReadOnlyDTO;
import gr.myproject.wod_core_backend.dto.UserInsertDTO;
import gr.myproject.wod_core_backend.dto.UserReadOnlyDTO;
import gr.myproject.wod_core_backend.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;
    private final SubscriptionMapper subscriptionMapper;

    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user) {
        UserReadOnlyDTO dto = new UserReadOnlyDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstname(user.getFirstname());
        dto.setLastname(user.getLastname());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setRole(user.getRole());
        dto.setGender(user.getGender());

        List<SubscriptionReadOnlyDTO> subs = user.getSubscriptions()
                .stream()
                .map(subscriptionMapper::mapToSubscriptionReadOnlyDTO)
                .toList();
        dto.setSubscriptions(subs);

        return dto;
    }

    public User mapToUserEntity(UserInsertDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setFirstname(dto.getFirstname());
        user.setLastname(dto.getLastname());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());
        user.setRole(Role.CUSTOMER); // default customer ειναι το role
        user.setIsActive(true); // default
        return user;
    }

}
