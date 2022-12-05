package msa.userservice.controller;

import msa.userservice.domain.UserEntity;
import msa.userservice.dto.UserDto;
import msa.userservice.service.UserService;
import msa.userservice.vo.Greeting;
import msa.userservice.vo.RequestUser;
import msa.userservice.vo.ResponseUser;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class UserController {

  private final Environment environment;
  private final UserService userService;

  @Autowired private Greeting greeting;

  @Autowired
  public UserController(Environment environment, UserService userService) {
    this.environment = environment;
    this.userService = userService;
  }

  @GetMapping("/health_check")
  public String status() {
    return String.format(
        "It's Working in User Service"
            + ", port(local.server.port)="
            + environment.getProperty("local.server.port")
            + ", port(server.port)="
            + environment.getProperty("server.port")
            + ", token.secret="
            + environment.getProperty("token.secret")
            + ", token.expiration_time="
            + environment.getProperty("token.expiration_time"));
  }

  @GetMapping("/welcome")
  public String welcome() {
    // return environment.getProperty("greeting.message");
    return greeting.getMessage();
  }

  @PostMapping("/users")
  public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserDto userDto = mapper.map(user, UserDto.class);
    userService.createUser(userDto);

    ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
  }

  @GetMapping("/users")
  public ResponseEntity<List<ResponseUser>> getUsers() {
    Iterable<UserEntity> users = userService.getUserByAll();

    List<ResponseUser> result = new ArrayList<>();
    users.forEach(
        v -> {
          result.add(new ModelMapper().map(v, ResponseUser.class));
        });

    return ResponseEntity.ok().body(result);
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<ResponseUser> getUser(@PathVariable("userId") String userId) {
    UserDto userDto = userService.getUserByUserId(userId);

    ResponseUser returnValue = new ModelMapper().map(userDto, ResponseUser.class);
    return ResponseEntity.ok().body(returnValue);
  }
}
