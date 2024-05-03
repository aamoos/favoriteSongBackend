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

    private String userName;

    private String password;            //사용자 비밀번호

    private String refreshToken;        //refresh token

    private boolean activated;          //활성화 여부

    //패스워드 변경
    public void changePassword(String password){
        this.password = password;
    }

    public void changeActivated(boolean active){
        this.activated = active;
    }

    public void changeRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "userSeq", referencedColumnName = "userSeq")},
            inverseJoinColumns = {@JoinColumn(name = "authorityName", referencedColumnName = "authorityName")})
    private Set<Authority> authorities;
}
