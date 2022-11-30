package msa.userservice.service;

import lombok.RequiredArgsConstructor;
import msa.userservice.domain.UserEntity;
import msa.userservice.domain.UserRepository;
import msa.userservice.dto.UserDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  public UserDto createUser(UserDto userDto) {
    userDto.setUserId(UUID.randomUUID().toString());

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserEntity userEntity = mapper.map(userDto, UserEntity.class);
    userEntity.setEncryptedPassword("encrypted password");

    userRepository.save(userEntity);

    return mapper.map(userEntity, UserDto.class);
  }
}
