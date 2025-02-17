package com.favoriteSongBackend.dto;

import lombok.Data;

@Data
public class EmailDto {

    @Data
    public static class Request {
        private String userId;              //사용자 아이디
        private String type;              //이메일 보내는 유형
    }

}
