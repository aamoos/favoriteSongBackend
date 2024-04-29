package com.favoriteSongBackend.dto;

import lombok.Data;
import lombok.Getter;

/**
 * 회원가입 요청 Dto
 */
public class SignupDto {

    @Data
    public static class Request {
        private String userId;              //사용자 아이디
        private String password;            //사용자 비밀번호
        private String userName;            //사용자 이름
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
