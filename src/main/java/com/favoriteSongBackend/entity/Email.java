package com.favoriteSongBackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import java.time.LocalDateTime;

/**
 * packageName    : com.hellchang.entity
 * fileName       : Email
 * author         : hj
 * date           : 2023-05-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-05-03        hj       최초 생성
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends BaseDateEntity {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;            //이메일 인덱스

    private String userId;      //로그인 아이디

    private String checkCode;   //랜덤 코드

    @Builder
    public Email(String userId, String checkCode){
        this.userId = userId;
        this.checkCode = checkCode;
    }
}
