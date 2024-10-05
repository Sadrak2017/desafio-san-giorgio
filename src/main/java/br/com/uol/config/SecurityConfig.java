package br.com.uol.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(
        auth -> auth
          .requestMatchers("/release-notes.html").permitAll()
          .requestMatchers("/actuator/health").permitAll()
          .requestMatchers("/actuator/metrics").permitAll()
          .requestMatchers("/actuator/metrics/**").permitAll()
          .requestMatchers("/san-giorgio/swagger-ui/index.html").permitAll()
          .requestMatchers("/san-giorgio/v1/actuator/**").permitAll()
          .requestMatchers("/san-giorgio/v1/recursos/**").permitAll()
          .requestMatchers("/san-giorgio/v3/**").permitAll()
          .requestMatchers("/san-giorgio/v3/swagger-ui/**").permitAll()
          .requestMatchers("/san-giorgio/api-docs/**").permitAll()
          .anyRequest().permitAll())
      .csrf().disable()
      .httpBasic(Customizer.withDefaults());
    return http.build();
  }
}
