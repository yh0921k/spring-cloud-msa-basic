package msa.userservice.service;

import msa.userservice.dto.UserDto;

public interface UserService {
  UserDto createUser(UserDto userDto);
}
