package vn.iotstar.service;

import vn.iotstar.dto.UserDTO;

public interface AuthService {
    UserDTO register(UserDTO userDto, String password);
}
