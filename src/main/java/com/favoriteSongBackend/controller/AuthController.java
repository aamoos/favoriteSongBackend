package com.favoriteSongBackend.controller;

import com.favoriteSongBackend.dto.EmailDto;
import com.favoriteSongBackend.dto.LoginDto;
import com.favoriteSongBackend.dto.SignupDto;
import com.favoriteSongBackend.dto.TokenDto;
import com.favoriteSongBackend.entity.Users;
import com.favoriteSongBackend.exception.CustomException;
import com.favoriteSongBackend.exception.ErrorCode;
import com.favoriteSongBackend.jwt.JwtFilter;
import com.favoriteSongBackend.jwt.TokenProvider;
import com.favoriteSongBackend.repository.UserRepository;
import com.favoriteSongBackend.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto){

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());

        Optional<Users> optionalUsers = userRepository.findByUserId(loginDto.getUserId());
        Users users = optionalUsers.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        //사용자가 비활성화일때
        if (!users.isActivated()) {
            throw new CustomException(ErrorCode.INACTIVE_USER);
        }
        else if(!passwordEncoder.matches(loginDto.getPassword(), users.getPassword())){
            //패스워드가 같지않을때
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        //authenticate -> loadByUserName
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.createToken(authentication);
        String refreshToken = tokenProvider.createRefreshToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + accessToken);

        return new ResponseEntity<>(new TokenDto(accessToken, refreshToken), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupDto.Request request){
        return ResponseEntity.ok(authService.signup(request));
    }

    //중복회원 체크
    @PostMapping("/signupCheck")
    public ResponseEntity<?> signUpCheck(@RequestBody SignupDto.Request request){
        return ResponseEntity.ok(authService.signupCheck(request));
    }

    @PostMapping("/passwordFind")
    public ResponseEntity<?> passwordFind(@RequestBody SignupDto.Request request) throws Exception {
        return ResponseEntity.ok(authService.passwordFind(request));
    }

    //비밀번호찾기 본인이 맞는지
    @PostMapping("/passwordFindCheck")
    public ResponseEntity<?> passwordFindCheck(@RequestBody SignupDto.Request request){
        return ResponseEntity.ok(authService.passwordFindCheck(request));
    }

    //이메일 발송
    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody EmailDto.Request request) throws Exception {

        //인증번호 발송
        if(request.getType().equals("checkCode")){
            authService.sendCheckCodeEmail(request);
        }
    }

}
