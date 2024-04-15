package com.favoriteSongBackend.dto;

import lombok.Data;

/**
 *  token Dto
 */
@Data
public class TokenDto {
    private String accessToken;           //토큰

    public TokenDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
