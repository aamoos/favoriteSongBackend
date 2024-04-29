package com.favoriteSongBackend.controller;

import com.favoriteSongBackend.dto.SongSearchDto;
import com.favoriteSongBackend.service.ApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiController {

    private final ApiService apiService;

    @PostMapping("/songSearch")
    //노래검색
    public ResponseEntity<?> songSearch(@RequestBody SongSearchDto.Request request) throws Exception {
        return ResponseEntity.ok(apiService.songSearch(request));
    }

}
