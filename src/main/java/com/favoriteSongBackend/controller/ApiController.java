package com.favoriteSongBackend.controller;

import com.favoriteSongBackend.dto.FavoriteSongDto;
import com.favoriteSongBackend.dto.SearchSongDto;
import com.favoriteSongBackend.service.ApiService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> songSearch(@RequestBody SearchSongDto.Request request, HttpServletRequest httpServletRequest) throws Exception {
        return ResponseEntity.ok(apiService.songSearch(request, httpServletRequest));
    }

    /**
     * 즐겨찾기 노래검색
     * @param request
     * @return
     * @throws Exception
     */

    @PostMapping("/searchFavoriteSong")
    //노래검색
    public ResponseEntity<?> searchFavoriteSong(@RequestBody SearchSongDto.Request request, HttpServletRequest httpServletRequest) throws Exception {
        return ResponseEntity.ok(apiService.songFavoriteSearch(request, httpServletRequest));
    }

    /**
     * 즐겨찾기
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/favoriteSong")
    //노래검색
    public ResponseEntity<?> songFavorite(@RequestBody FavoriteSongDto.Request request, HttpServletRequest httpServletRequest) throws Exception {
        return ResponseEntity.ok(apiService.songFavorite(request, httpServletRequest));
    }

}
