package vn.iotstar.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.iotstar.entity.User;
import vn.iotstar.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsernameOrEmail(login, login)
                .orElseGet(() -> userRepository.findByUsernameOrEmailIgnoreCase(login)
                        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy username/email: " + login)));

        String roleName = (user.getRole() != null) ? user.getRole().getName() : "ROLE_USER";
        if (roleName != null && !roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        return new CustomUserDetails(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                user.getFullName(),
                user.getImages(),
                roleName,
                user.isEnabled()
        );
    }
}
