package msa.userservice.service;

import lombok.extern.slf4j.Slf4j;
import msa.userservice.client.OrderServiceClient;
import msa.userservice.domain.UserEntity;
import msa.userservice.domain.UserRepository;
import msa.userservice.dto.UserDto;
import msa.userservice.vo.ResponseOrder;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  private final Environment environment;
  private final OrderServiceClient orderServiceClient;
  private final RestTemplate restTemplate;

  @Autowired
  public UserServiceImpl(
      UserRepository userRepository,
      BCryptPasswordEncoder passwordEncoder,
      Environment environment,
      RestTemplate restTemplate,
      OrderServiceClient orderServiceClient) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.environment = environment;
    this.restTemplate = restTemplate;
    this.orderServiceClient = orderServiceClient;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByEmail(username);
    if (userEntity == null) {
      throw new UsernameNotFoundException(username);
    }

    return new User(
        userEntity.getEmail(),
        userEntity.getEncryptedPassword(),
        true,
        true,
        true,
        true,
        new ArrayList<>());
  }

  @Override
  public UserDto createUser(UserDto userDto) {
    userDto.setUserId(UUID.randomUUID().toString());

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserEntity userEntity = mapper.map(userDto, UserEntity.class);

    userEntity.setEncryptedPassword(passwordEncoder.encode(userDto.getPassword()));

    userRepository.save(userEntity);

    return mapper.map(userEntity, UserDto.class);
  }

  @Override
  public UserDto getUserByUserId(String userId) {
    UserEntity userEntity = userRepository.findByUserId(userId);
    if (userEntity == null) {
      throw new UsernameNotFoundException("User not found");
    }

    UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);
    //    List<ResponseOrder> orders = new ArrayList<>();

    //    String orderUrl = String.format(environment.getProperty("order_service.url"), userId);
    //    ResponseEntity<List<ResponseOrder>> orderListResponse =
    //        restTemplate.exchange(
    //            orderUrl,
    //            HttpMethod.GET,
    //            null,
    //            new ParameterizedTypeReference<List<ResponseOrder>>() {});
    //
    //    List<ResponseOrder> ordersList = orderListResponse.getBody();

    //    List<ResponseOrder> ordersList = null;
    //    try {
    //      ordersList = orderServiceClient.getOrders(userId);
    //    } catch (FeignException exception) {
    //      log.error(exception.getMessage());
    //    }
    List<ResponseOrder> ordersList = orderServiceClient.getOrders(userId);
    userDto.setOrders(ordersList);

    return userDto;
  }

  @Override
  public Iterable<UserEntity> getUserByAll() {
    return userRepository.findAll();
  }

  @Override
  public UserDto getUserDetailsByEmail(String username) {
    UserEntity userEntity = userRepository.findByEmail(username);
    if (userEntity == null) {
      throw new UsernameNotFoundException(username);
    }

    return new ModelMapper().map(userEntity, UserDto.class);
  }
}
