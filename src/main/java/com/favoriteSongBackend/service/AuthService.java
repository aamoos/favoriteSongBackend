package com.favoriteSongBackend.service;

import com.favoriteSongBackend.common.PasswordGenerator;
import com.favoriteSongBackend.dto.EmailDto;
import com.favoriteSongBackend.dto.SignDto;
import com.favoriteSongBackend.dto.SignUpDto;
import com.favoriteSongBackend.entity.Authority;
import com.favoriteSongBackend.entity.Email;
import com.favoriteSongBackend.entity.Users;
import com.favoriteSongBackend.exception.CustomException;
import com.favoriteSongBackend.exception.ErrorCode;
import com.favoriteSongBackend.repository.EmailRepository;
import com.favoriteSongBackend.repository.UserRepository;
import jakarta.mail.MessagingException;
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
    private final EmailRepository emailRepository;
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.username}")
    private String FROM_ADDRESS;

    /**
     * 회원 가입
     * @param request
     * @return
     */

    @Transactional
    public ResponseEntity<?> signup(SignUpDto.Request request) {
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

        return ResponseEntity.ok(new SignDto.Response(userRepository.save(user).getUserSeq()));
    }
    
    //가입되어있는 유저인지 체크
    public ResponseEntity<?> signupCheck(SignDto.Request request){

        if (userRepository.findByUserId(request.getUserId()).orElse(null) != null) {
            throw new CustomException(ErrorCode.CONFLICT);
        }
        return ResponseEntity.ok(userRepository.findByUserId(request.getUserId()).orElse(null) == null);
    }

    @Transactional
    public ResponseEntity<?> passwordFind(SignDto.Request request) throws Exception {
        Email email = emailRepository.findFirstByUserIdOrderByCreatedDateDesc(request.getUserId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "임시비밀번호가 없습니다."));

        //임시비밀번호가 같지않는경우
        if(!email.getCheckCode().equals(request.getCheckCode())){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Users users =  userRepository.findByUserId(request.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String tempPassword = PasswordGenerator.tempRandomPassword(10);

        users.changePassword(passwordEncoder.encode(tempPassword));

        //임시 비밀번호 발송
        sendTempPasswordEmail(request, tempPassword);

        return ResponseEntity.ok(tempPassword);
    }

    //아이디, 이름으로 가입된 유저인지 체크
    public ResponseEntity<?> passwordFindCheck(SignDto.Request request){
        return ResponseEntity.ok(userRepository.findByUserIdAndUserName(request.getUserId(), request.getUserName()).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
    }

    //인증번호 발송
    public void sendCheckCodeEmail(EmailDto.Request request) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(request.getUserId());
        helper.setFrom(FROM_ADDRESS);
        helper.setSubject("<favoriteSong> 인증번호 보내드립니다.");

        // create the Thymeleaf context object and add the name variable
        Context thymeleafContext = new Context();

        String checkCode = PasswordGenerator.generateRandomCheckCode(6);

        Email email = Email.builder()
                .userId(request.getUserId())
                .checkCode(checkCode)     //임시비밀번호 6자리 랜덤
                .build();

        thymeleafContext.setVariable("checkCode", checkCode);
        emailRepository.save(email);

        // generate the HTML content from the Thymeleaf template
        String htmlContent = thymeleafTemplateEngine.process("email.html", thymeleafContext);

        helper.setText(htmlContent, true);
        mailSender.send(message);
        log.info("메일 전송 완료 ----------------------------------------");
    }

    //임시 비밀번호 발송
    private void sendTempPasswordEmail(SignDto.Request request, String tempPassword) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(request.getUserId());
        helper.setFrom(FROM_ADDRESS);
        helper.setSubject("<favoriteSong> 임시비밀번호 보내드립니다.");

        // create the Thymeleaf context object and add the name variable
        Context thymeleafContext = new Context();

        thymeleafContext.setVariable("tempPassword", tempPassword);

        // generate the HTML content from the Thymeleaf template
        String htmlContent = thymeleafTemplateEngine.process("tempPassword.html", thymeleafContext);

        helper.setText(htmlContent, true);
        mailSender.send(message);
        log.info("메일 전송 완료 ----------------------------------------");
    }

}
