package msa.userservice.controller;

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

  @GetMapping("/user-service/health_check")
  public String status() {
    return String.format(
        "It's Working in User Service on PORT %s", environment.getProperty("local.server.port"));
  }

  @GetMapping("/user-service/welcome")
  public String welcome() {
    // return environment.getProperty("greeting.message");
    return greeting.getMessage();
  }

  @PostMapping("/user-service/users")
  public ResponseEntity<ResponseUser> createUser(@RequestBody RequestUser user) {

    ModelMapper mapper = new ModelMapper();
    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserDto userDto = mapper.map(user, UserDto.class);
    userService.createUser(userDto);

    ResponseUser responseUser = mapper.map(userDto, ResponseUser.class);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseUser);
  }
}
