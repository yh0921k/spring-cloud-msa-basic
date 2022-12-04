package msa.userservice.service;

import msa.userservice.domain.UserEntity;
import msa.userservice.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
  UserDto createUser(UserDto userDto);

  UserDto getUserByUserId(String userId);

  Iterable<UserEntity> getUserByAll();

  UserDto getUserDetailsByEmail(String username);
}
