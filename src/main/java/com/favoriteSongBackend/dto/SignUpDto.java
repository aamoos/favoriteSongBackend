package com.favoriteSongBackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

/**
 * 회원가입 요청 Dto
 */
public class SignUpDto {

    @Data
    public static class Request {

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "올바른 이메일을 입력해주세요.")
        private String userId;              //사용자 아이디
        @NotBlank(message = "패스워드를 입력해주세요.")
        private String password;            //사용자 비밀번호
        @NotBlank(message = "이름을 입력해주세요.")
        private String userName;            //사용자 이름
        @NotBlank(message = "인증번호를 입력해주세요.")
        private String checkCode;           //인증번호
    }

    @Getter
    public static class Response {
        private Long userSeq;

        public Response(Long userSeq) {
            this.userSeq = userSeq;
        }
    }

}
