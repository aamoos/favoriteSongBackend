package com.favoriteSongBackend.controller;

import com.favoriteSongBackend.dto.FavoriteSongDto;
import com.favoriteSongBackend.dto.SearchSongDto;
import com.favoriteSongBackend.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    /**
     * 노래검색
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/searchSong")
    //노래검색
    public ResponseEntity<?> songSearch(@RequestBody SearchSongDto.Request request) throws Exception {
        return ResponseEntity.ok(apiService.songSearch(request));
    }

    /**
     * 즐겨찾기
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/favoriteSong")
    //노래검색
    public ResponseEntity<?> songFavorite(@RequestBody FavoriteSongDto.Request request) throws Exception {
        return ResponseEntity.ok(apiService.songFavorite(request));
    }

}
