package msa.userservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    // http.authorizeRequests().antMatchers("/users/**").permitAll();

    // 모든 경로에 대해 아이피가 제한되며, 필터를 통과한 데이터에 대해서만 권한 부여 및 작업 가능
    http.authorizeRequests()
        .antMatchers("/**")
        .hasIpAddress("192.168.0.3")
        .and()
        .addFilter(getAuthenticationFilter());

    http.headers().frameOptions().disable();
  }

  private AuthenticationFilter getAuthenticationFilter() throws Exception {
    AuthenticationFilter authenticationFilter = new AuthenticationFilter();
    authenticationFilter.setAuthenticationManager(authenticationManager());

    return authenticationFilter;
  }
}
