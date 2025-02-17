package com.favoriteSongBackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  즐겨찾기 entity
 */
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteSong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private Long no;
    private String singer;
    private String title;
    private String userId;
    private String composer;    //작곡가
    private String lyricist;    //작사가
    private String releaseDate;     //출시일

}
