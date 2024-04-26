package com.favoriteSongBackend.controller;

import com.favoriteSongBackend.dto.LoginDto;
import com.favoriteSongBackend.dto.SignupDto;
import com.favoriteSongBackend.dto.TokenDto;
import com.favoriteSongBackend.jwt.JwtFilter;
import com.favoriteSongBackend.jwt.TokenProvider;
import com.favoriteSongBackend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto){

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUserId(), loginDto.getPassword());

        //authenticate -> loadByUserName
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
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
    public ResponseEntity<?> passwordFind(@RequestBody SignupDto.Request request){
        return ResponseEntity.ok(authService.passwordFind(request));
    }

    //비밀번호찾기 본인이 맞는지
    @PostMapping("/passwordFindCheck")
    public ResponseEntity<?> passwordFindCheck(@RequestBody SignupDto.Request request){
        return ResponseEntity.ok(authService.passwordFindCheck(request));
    }

    //이메일 발송
    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody SignupDto.Request request) throws Exception {
        authService.sendEmail(request.getUserId());
    }

}
