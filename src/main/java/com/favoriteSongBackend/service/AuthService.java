package com.favoriteSongBackend.service;

import com.favoriteSongBackend.common.PasswordGenerator;
import com.favoriteSongBackend.dto.SignupDto;
import com.favoriteSongBackend.entity.Authority;
import com.favoriteSongBackend.entity.Email;
import com.favoriteSongBackend.entity.Users;
import com.favoriteSongBackend.exception.CustomException;
import com.favoriteSongBackend.exception.ErrorCode;
import com.favoriteSongBackend.repository.EmailRepository;
import com.favoriteSongBackend.repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailRepository emailRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;

    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;

    /**
     * 회원 가입
     * @param request
     * @return
     */

    @Transactional
    public ResponseEntity<?> signup(SignupDto.Request request) {
        if (userRepository.findByUserId(request.getUserId()).orElse(null) != null) {
            throw new CustomException(ErrorCode.CONFLICT);
        }

        Email email = emailRepository.findFirstByUserIdOrderByCreatedDateDesc(request.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "임시비밀번호가 없습니다."));

        //임시비밀번호가 같지않는경우
        if(!email.getCheckCode().equals(request.getCheckCode())){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
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

        return ResponseEntity.ok(new SignupDto.Response(userRepository.save(user).getUserSeq()));
    }
    
    //가입되어있는 유저인지 체크
    public ResponseEntity<?> signupCheck(SignupDto.Request request){

        if (userRepository.findByUserId(request.getUserId()).orElse(null) != null) {
            throw new CustomException(ErrorCode.CONFLICT);
        }
        return ResponseEntity.ok(userRepository.findByUserId(request.getUserId()).orElse(null) == null);
    }

    //임시비밀번호 발송
    public void sendEmail(String userId) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(userId);
        helper.setFrom(FROM_ADDRESS);
        helper.setSubject("<favoriteSong> 임시비밀번호 보내드립니다.");

        // create the Thymeleaf context object and add the name variable
        Context thymeleafContext = new Context();

        String checkCode = PasswordGenerator.generateRandomPassword(6);

        Email email = Email.builder()
                .userId(userId)
                .checkCode(checkCode)     //임시비밀번호 6자리 랜덤
                .build();

        thymeleafContext.setVariable("userId", userId);
        thymeleafContext.setVariable("checkCode", checkCode);
        emailRepository.save(email);

        // generate the HTML content from the Thymeleaf template
        String htmlContent = thymeleafTemplateEngine.process("email.html", thymeleafContext);

        helper.setText(htmlContent, true);
        mailSender.send(message);
        log.info("메일 전송 완료 ----------------------------------------");
    }

}
