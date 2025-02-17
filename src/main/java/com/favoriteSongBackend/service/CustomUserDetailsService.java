package com.favoriteSongBackend.service;

import com.favoriteSongBackend.entity.Users;
import com.favoriteSongBackend.exception.CustomException;
import com.favoriteSongBackend.exception.ErrorCode;
import com.favoriteSongBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *  로그인시 사용자 인증
 */

@Component("userDetailsService")
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String userId) throws CustomException {

        Optional<Users> optionalUsers = userRepository.findByUserId(userId);
        Users users = optionalUsers.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        return createUser(users);
    }

    private org.springframework.security.core.userdetails.User createUser(Users users) {
        if(!users.isActivated()){
            throw new CustomException(ErrorCode.INACTIVE_USER);
        }

        List<GrantedAuthority> grantedAuthorities = users.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return new User(users.getUserId(),
                users.getPassword(),
                grantedAuthorities);
    }
}
