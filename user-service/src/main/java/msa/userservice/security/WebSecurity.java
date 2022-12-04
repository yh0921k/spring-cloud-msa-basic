package msa.userservice.security;

import msa.userservice.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

  private final UserService userService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final Environment environment;

  public WebSecurity(
      UserService userService,
      BCryptPasswordEncoder bCryptPasswordEncoder,
      Environment environment) {
    this.userService = userService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.environment = environment;
  }

  // 권한에 관련된 설정
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    // http.authorizeRequests().antMatchers("/users/**").permitAll();

    // 모든 경로에 대해 아이피가 제한되며, 필터를 통과한 데이터에 대해서만 권한 부여 및 작업 가능
    http.authorizeRequests()
        .antMatchers("/error/**")
        .permitAll()
        .antMatchers("/**")
        .access(
            "hasIpAddress('127.0.0.1') or hasIpAddress('192.168.60.103/24') or hasIpAddress('0:0:0:0:0:0:0:1')")
        .and()
        .addFilter(getAuthenticationFilter());

    http.headers().frameOptions().disable();
  }

  private AuthenticationFilter getAuthenticationFilter() throws Exception {
    AuthenticationFilter authenticationFilter =
        new AuthenticationFilter(authenticationManager(), userService, environment);
    // authenticationFilter.setAuthenticationManager(authenticationManager());

    return authenticationFilter;
  }

  // 인증에 관련된 설정
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // 사용자 전달 데이터를 기반으로 인증 처리 수행
    // select password from users where email = ?;
    // 데이터베이스의 패스워드를 사용자 입력 패스워드를 비교
    // userDetailService 메서드를 보면 전달되는 UserDetailsService를 상속받아야함
    auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);
  }
}
