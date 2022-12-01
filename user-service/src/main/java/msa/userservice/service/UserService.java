package msa.userservice.service;

import msa.userservice.domain.UserEntity;
import msa.userservice.dto.UserDto;

public interface UserService {
  UserDto createUser(UserDto userDto);

  UserDto getUserByUserId(String userId);

  Iterable<UserEntity> getUserByAll();
}
