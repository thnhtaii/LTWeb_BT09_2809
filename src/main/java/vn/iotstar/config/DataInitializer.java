package vn.iotstar.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            Role userRole = roleRepository
                    .findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name("ROLE_USER")
                                    .build()
                    ));

            Role adminRole = roleRepository
                    .findByName("ROLE_ADMIN")
                    .orElseGet(() -> roleRepository.save(
                            Role.builder()
                                    .name("ROLE_ADMIN")
                                    .build()
                    ));

            if (userRepository.findByUsername("user01").isEmpty() && userRepository.findByEmail("user01@gmail.com").isEmpty()) {
                User user = User.builder()
                        .username("user01")
                        .email("user01@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Nguyễn Hữu Trung")
                        .images("/images/user.png")
                        .role(userRole)
                        .enabled(true)
                        .build();
                userRepository.save(user);
            }

            if (userRepository.findByUsername("thanhtai").isEmpty() && userRepository.findByEmail("thanhtai@hcmute.edu.vn").isEmpty()) {
                User admin = User.builder()
                        .username("thanhtai")
                        .email("thanhtai@hcmute.edu.vn")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Đỗ Thành Thanh Tài")
                        .images("/images/user.png")
                        .role(adminRole)
                        .enabled(true)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
