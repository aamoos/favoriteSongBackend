package com.favoriteSongBackend.service;

import com.favoriteSongBackend.dto.FavoriteSongDto;
import com.favoriteSongBackend.dto.SearchSongDto;
import com.favoriteSongBackend.dto.SignupDto;
import com.favoriteSongBackend.entity.FavoriteSong;
import com.favoriteSongBackend.repository.FavoriteSongRepository;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiService {

    private final FavoriteSongRepository favoriteSongRepository;

    @Transactional
    public ResponseEntity<?> songSearch(SearchSongDto.Request request) throws Exception {

        SslContext context = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        HttpClient httpClient = HttpClient.create().secure(provider -> provider.sslContext(context));
        String httpUrl = "https://api.manana.kr/v2/karaoke/search.json" + getMakeParam(request);

        log.info("httpUrl = {}", httpUrl);

        // webClient 기본 설정
        WebClient webClient = WebClient.builder()
                .baseUrl(httpUrl) // HTTPS 주소로 변경
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();

        SearchSongDto.SongSearchResponseDto response = webClient
                .get()
                .uri(httpUrl)
                .retrieve()
                .bodyToMono(SearchSongDto.SongSearchResponseDto.class)
                .block();

        log.info("response = {}", response);

        return ResponseEntity.ok(response);
    }

    @Transactional
    public ResponseEntity<?> songFavorite(FavoriteSongDto.Request request){

        Optional<FavoriteSong> existingFavorite = favoriteSongRepository.findByBrandAndNo(request.getBrand(), request.getNo());

        // 기존 값이 존재하면 삭제
        existingFavorite.ifPresent(favoriteSongRepository::delete);

        return ResponseEntity.ok(new FavoriteSongDto.Response(favoriteSongRepository.save(request.toEntity()).getId()));
    }

    private String getMakeParam(SearchSongDto.Request request){
        String result = "";

        //kumyoung / tj / dam / joysound
        if(StringUtils.hasText(request.getBrand().trim())){
            result+="?brand="+request.getBrand();
        }

        //제목
        if("title".equals(request.getSearchType())){
            result+="&title="+request.getSearchVal();
        }

        //가수
        if("singer".equals(request.getSearchType())){
            result+="&singer="+request.getSearchVal();
        }

        //번호
        if("no".equals(request.getSearchType())){
            result+="&no="+request.getSearchVal();
        }

        //페이징
        result+="&limit="+request.getLimit();
        result+="&offset="+request.getOffset();

        return result;
    }
}
