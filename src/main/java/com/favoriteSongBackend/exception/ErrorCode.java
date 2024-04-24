package com.favoriteSongBackend.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    /*
     * 401 UNAUTHORIZED: 인증 실패
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "임시 비밀번호가 잘못되었습니다. 다시 입력해주세요."),

    /*
     * 409 CONFLICT: 이미 존재하는 회원
     */
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 회원입니다."),

    /*
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),

    /*
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),

    ;

    private final HttpStatus status;
    private final String message;

}
