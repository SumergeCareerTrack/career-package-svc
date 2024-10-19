package com.sumerge.careertrack.career_package_svc.configs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.sumerge.careertrack.career_package_svc.filters.RedisTokenFilter;

import jakarta.servlet.DispatcherType;

@Slf4j
@Configuration
@EnableWebSecurity // (debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final RedisTokenFilter redisTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                // Expose the Content-Disposition heade
                .addFilterAfter(redisTokenFilter, BasicAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
