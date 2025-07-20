package com.madhu.PatientMangement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class SecurityConfig {

  @Bean
  public AuthenticationEntryPoint patientApiEntryPoint() {
    return new BasicAuthenticationEntryPoint() {
      @Override
      public void afterPropertiesSet() {
        setRealmName("PatientAPI");
        super.afterPropertiesSet();
      }

      // NOTE: only throws IOException now
      @Override
      public void commence(HttpServletRequest request,
                           HttpServletResponse response,
                           AuthenticationException authException)
          throws IOException {

        // send back 401 + WWW‑Authenticate header
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE,
                           "Basic realm=\"" + getRealmName() + "\"");
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
      }
    };
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .csrf(csrf -> csrf.disable())
      .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
      .httpBasic(basic -> basic.authenticationEntryPoint(patientApiEntryPoint()));
    return http.build();
  }
}
