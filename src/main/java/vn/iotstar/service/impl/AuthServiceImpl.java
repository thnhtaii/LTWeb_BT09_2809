package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.iotstar.dto.UserDTO;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.mapper.UserMapper;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO register(UserDTO userDto, String password) {
        Role role = null;
        if (userDto.getRoleName() != null) {
            role = roleRepository.findByName(userDto.getRoleName())
                    .or(() -> roleRepository.findByNameIgnoreCase(userDto.getRoleName()))
                    .orElse(null);
        }
        if (role == null) {
            role = roleRepository.findByName("ROLE_USER")
                    .orElseGet(() -> roleRepository.save(new Role("ROLE_USER")));
        }

        User user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);
        user.setEnabled(true);

        User saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }
}
