package com.favoriteSongBackend.service;

import com.favoriteSongBackend.dto.SignupDto;
import com.favoriteSongBackend.entity.Authority;
import com.favoriteSongBackend.entity.Users;
import com.favoriteSongBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원 가입
     * @param request
     * @return
     */

    @Transactional
    public SignupDto.Response signup(SignupDto.Request request) {
        if (userRepository.findByUserId(request.getUserId()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();

        Users user = Users.builder()
                .userId(request.getUserId())
                .password(passwordEncoder.encode(request.getPassword()))
                .userName(request.getUserName())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();

        return new SignupDto.Response(userRepository.save(user).getUserSeq());
    }

}
