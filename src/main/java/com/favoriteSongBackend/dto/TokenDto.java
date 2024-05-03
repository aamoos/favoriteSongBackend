package com.favoriteSongBackend.dto;

import lombok.Data;

/**
 *  token Dto
 */
@Data
public class TokenDto {
    private String accessToken;             //토큰
    private String refreshToken;            //리프레시 토큰

    public TokenDto() {
        // Default constructor
    }

    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
