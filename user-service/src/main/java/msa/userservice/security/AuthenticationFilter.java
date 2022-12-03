package msa.userservice.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import msa.userservice.vo.RequestLogin;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      // Request Body에 담긴 정보를 필터에서 사용하기 위해 getInputStream()로 데이터를 추출하지만 최초 1회만 가능
      RequestLogin credentials =
          new ObjectMapper().readValue(request.getInputStream(), RequestLogin.class);

      // 사용자 입력인 이메일, 주소를 스프링 시큐리티가 사용할 수 있는 값으로 변환하기 위해 UsernamePasswordAuthenticationToken 사용
      // 세 번째 인자는 권한 리스트
      UsernamePasswordAuthenticationToken authenticationToken =
          new UsernamePasswordAuthenticationToken(
              credentials.getEmail(), credentials.getPassword(), new ArrayList<>());

      // AuthenticationManager에게 아이디와 패스워드 비교를 위임
      return getAuthenticationManager().authenticate(authenticationToken);
    } catch (IOException exception) {
      throw new RuntimeException(exception);
    }
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException, ServletException {
    // super.successfulAuthentication(request, response, chain, authResult);
    // 추후 로그인 성공시 작업을 서술할 수 있(토큰 생성, 로그인시 반환값 설정 등)
  }
}
