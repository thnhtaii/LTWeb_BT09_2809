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

            userRepository.findByUsername("user01").ifPresentOrElse(
                    user -> {
                        user.setFullName("Đỗ Thanh Thành Tài");
                        userRepository.save(user);
                    },
                    () -> {
                        if (userRepository.findByEmail("user01@gmail.com").isEmpty()) {
                            User user = User.builder()
                                    .username("user01")
                                    .email("user01@gmail.com")
                                    .password(passwordEncoder.encode("123456"))
                                    .fullName("Đỗ Thanh Thành Tài")
                                    .role(userRole)
                                    .enabled(true)
                                    .build();
                            userRepository.save(user);
                        }
                    }
            );

            if (userRepository.findByUsername("admin").isEmpty() && userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("123456"))
                        .fullName("Administrator")
                        .role(adminRole)
                        .enabled(true)
                        .build();
                userRepository.save(admin);
            }
        };
    }
}
