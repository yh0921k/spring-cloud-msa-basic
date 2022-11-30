package msa.userservice.controller;

import msa.userservice.vo.Greeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserController {

  private final Environment environment;

  @Autowired
  private Greeting greeting;

  @Autowired
  public UserController(Environment environment) {
    this.environment = environment;
  }

  @GetMapping("/health_check")
  public String status() {
    return "It's Working in User Service";
  }

  @GetMapping("/welcome")
  public String welcome() {
    // return environment.getProperty("greeting.message");
    return greeting.getMessage();
  }
}
