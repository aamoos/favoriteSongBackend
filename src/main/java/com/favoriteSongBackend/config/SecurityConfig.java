package com.favoriteSongBackend.config;

import com.favoriteSongBackend.jwt.JwtAccessDeniedHandler;
import com.favoriteSongBackend.jwt.JwtAuthenticationEntryPoint;
import com.favoriteSongBackend.jwt.JwtSecurityConfig;
import com.favoriteSongBackend.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)                                              //토큰 사용하므로 csrf disable
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                .headers((headerConfig) ->
                        headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable     //h2-console을 위한 설정
                        )
                )

                .sessionManagement(sessionManagement -> sessionManagement                           //session을 사용하지 않기 때문에 STATELESS로 설정
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests
                                .requestMatchers(PathRequest.toH2Console()).permitAll()              //h2 console 관련 security 허용
                                .requestMatchers("/szs/signup", "/szs/login", "/3o3/swagger.html", "/3o3/swagger-ui/**", "/api-docs/**").permitAll()
                                .anyRequest().authenticated()
                )

                .with(new JwtSecurityConfig(tokenProvider), customizer -> {
                });

        return http.build();
    }

}
