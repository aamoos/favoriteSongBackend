package com.favoriteSongBackend.dto;

import com.favoriteSongBackend.entity.FavoriteSong;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;

public class FavoriteListDto {
    @Data
    public static class Request {
        private Long id;
        private String brand;       //brand
        private Long no;            //노래번호
        private String singer;      //가수명
        private String title;       //제목
        private String userId;      //등록자

        /* Dto -> Entity */
        public FavoriteSong toEntity() {
            return FavoriteSong.builder()
                    .id(id)
                    .brand(brand)
                    .no(no)
                    .singer(singer)
                    .title(title)
                    .userId(userId)
                    .build();

        }
    }

    @Getter
    public static class Response {
        private Long id;
        private String brand;
        private Long no;
        private String singer;
        private String title;
        private String userId;
        private String composer;
        private String lyricist;
        private String release;

        public Response(FavoriteSong favoriteSong) {
            this.id = favoriteSong.getId();
            this.brand = favoriteSong.getBrand();
            this.no = favoriteSong.getNo();
            this.singer = favoriteSong.getSinger();
            this.title = favoriteSong.getTitle();
            this.userId = favoriteSong.getUserId();
            this.composer = favoriteSong.getComposer();
            this.lyricist = favoriteSong.getLyricist();
            this.release = favoriteSong.getReleaseDate();
        }

        @QueryProjection
        public Response(Long id, String brand, Long no,
                        String singer, String title, String userId
                , String composer, String lyricist, String releaseDate) {
            this.id = id;
            this.brand = brand;
            this.no = no;
            this.singer = singer;
            this.title = title;
            this.userId = userId;
            this.composer = composer;
            this.lyricist = lyricist;
            this.release = releaseDate;
        }
    }
}
