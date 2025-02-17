package com.favoriteSongBackend.dto;

import com.favoriteSongBackend.entity.FavoriteSong;
import lombok.Data;
import lombok.Getter;

public class FavoriteSongDto {
    @Data
    public static class Request {
        private Long id;
        private String brand;       //brand
        private Long no;            //노래번호
        private String singer;      //가수명
        private String title;       //제목
        private String userId;      //등록자
        private String composer;    //작곡가
        private String lyricist;    //작사가
        private String releaseDate;     //출시일

        /* Dto -> Entity */
        public FavoriteSong toEntity() {
            return FavoriteSong.builder()
                    .id(id)
                    .brand(brand)
                    .no(no)
                    .singer(singer)
                    .title(title)
                    .userId(userId)
                    .composer(composer)
                    .lyricist(lyricist)
                    .releaseDate(releaseDate)
                    .build();

        }
    }

    @Getter
    public static class Response {
        private boolean liked;

        public Response(boolean liked) {
            this.liked = liked;
        }
    }
}
