package com.favoriteSongBackend.dto;

import lombok.Data;

/**
 *  로그인 Dto
 */
@Data
public class LoginDto {
    private String userId;          //사용자 아이디
    private String password;        //사용자 패스워드
}
