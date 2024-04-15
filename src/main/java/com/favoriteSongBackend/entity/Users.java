package com.favoriteSongBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Set;

/**
 *  사용자 entity
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;               //사용자 시퀀스

    private String userId;              //사용자 아이디

    private String password;            //사용자 비밀번호

    private String name;                //사용자 이름

    private String regNo;               //사용자 주민등록번호

    private boolean activated;          //활성화 여부

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "userSeq", referencedColumnName = "userSeq")},
            inverseJoinColumns = {@JoinColumn(name = "authorityName", referencedColumnName = "authorityName")})
    private Set<Authority> authorities;
}
