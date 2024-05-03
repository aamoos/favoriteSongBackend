package com.favoriteSongBackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchSongDto {

    @Data
    public static class Request {
        private String brand;               // kumyoung, tj, dam, joysound
        private String searchType;          // title, singer, no
        private String searchVal;
        private String searchDate;
        private int limit;
        private int offset;
        private String userId;
        private String urlTarget;
    }

    @Data
    public static class SongResponseDto {
        private String brand;
        private Long no;
        private String title;
        private String singer;
        private String composer;
        private String lyricist;
        private String release;
        private boolean liked;
    }

    @Data
    public static class SongSearchResponseDto {
        private TotalInfo total;
        private int page;
        private int offset;
        private int limit;
        private List<SongResponseDto> data;

        public SongSearchResponseDto(TotalInfo total, int page, int offset, int limit, List<SongResponseDto> data) {
            this.total = total;
            this.page = page;
            this.offset = offset;
            this.limit = limit;
            this.data = data;
        }

        public SongSearchResponseDto() {
        }
    }

    @Data
    public static class TotalInfo {
        private int row;
        private int page;

        public TotalInfo(int row, int page) {
            this.row = row;
            this.page = page;
        }

        public TotalInfo() {
        }
    }

}
