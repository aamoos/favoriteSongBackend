package com.favoriteSongBackend.dto;

import lombok.Data;

/**
 * 회원가입 요청 Dto
 */
@Data
public class SignupDto {

    @Data
    public static class Request {
        private String userId;              //사용자 아이디
        private String password;            //사용자 비밀번호
        private String name;                //사용자 이름
        private String regNo;               //사용자 주민등록번호
    }

    @Data
    public static class Response {
        private Long userSeq;

        public Response(Long userSeq) {
            this.userSeq = userSeq;
        }
    }

}
