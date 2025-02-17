package com.favoriteSongBackend.service;

import com.favoriteSongBackend.dto.*;
import com.favoriteSongBackend.entity.FavoriteSong;
import com.favoriteSongBackend.jwt.TokenProvider;
import com.favoriteSongBackend.repository.FavoriteSongRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.favoriteSongBackend.entity.QFavoriteSong.favoriteSong;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiService {

    private final FavoriteSongRepository favoriteSongRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Value("${spring.karaoke.search-url}")
    private String searchUrl;

    @Value("${spring.karaoke.release-url}")
    private String releaseUrl;

    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseEntity<?> songSearch(SearchSongDto.Request request, HttpServletRequest httpServletRequest) throws Exception {

        SslContext context = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        HttpClient httpClient = HttpClient.create().secure(provider -> provider.sslContext(context));
        String httpUrl = getRequestUrl(request);

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

        List<SearchSongDto.SongResponseDto> data = null;
        if (response != null) {
            data = response.getData();
        }

        request.setUserId(tokenProvider.getUsernameFromToken(tokenProvider.getJwtToken(httpServletRequest)));
        List<FavoriteSong> favoriteSongs = favoriteSongRepository.findByUserId(request.getUserId());

        // 내가 좋아하는 곡번호 리스트
        List<Long> noList = favoriteSongs.stream()
                .map(FavoriteSong::getNo)
                .toList();

        //내가 좋아하는 곡번호 리스트와 조회한 곡 리스트 번호가 같은경우 liked true, else -> false
        if (data != null) {
            for (SearchSongDto.SongResponseDto datum : data) {
                datum.setLiked(noList.contains(datum.getNo()));
            }
        }

        return ResponseEntity.ok(response);
    }

    @Transactional
    public List<FavoriteListDto.Response> songFavoriteSearch(SearchSongDto.Request request, HttpServletRequest httpServletRequest){
        request.setUserId(tokenProvider.getUsernameFromToken(tokenProvider.getJwtToken(httpServletRequest)));
        return jpaQueryFactory
            .select(new QFavoriteListDto_Response(
                    favoriteSong.id,
                    favoriteSong.brand,
                    favoriteSong.no,
                    favoriteSong.singer,
                    favoriteSong.title,
                    favoriteSong.userId,
                    favoriteSong.composer,
                    favoriteSong.lyricist,
                    favoriteSong.releaseDate
            ))
            .from(favoriteSong)
            .where(
                 favoriteSong.brand.eq(request.getBrand())
                ,favoriteSong.userId.eq(request.getUserId())
                ,eqSearchCondition(request)
            )
            .fetch();
    }

    private BooleanExpression eqSearchCondition(SearchSongDto.Request request) {
        String searchType = request.getSearchType();
        String searchVal = request.getSearchVal();

        if(searchType.equals("title")){
            return StringUtils.hasText(searchVal) ? favoriteSong.title.contains(searchVal) : null;
        }else if(searchType.equals("singer")){
            return StringUtils.hasText(searchVal) ? favoriteSong.singer.contains(searchVal) : null;
        }else if(searchType.equals("no")){
            return StringUtils.hasText(searchVal) ? favoriteSong.no.eq(Long.valueOf(searchVal)) : null;
        }
        return null;
    }

    @Transactional
    public ResponseEntity<?> songFavorite(FavoriteSongDto.Request request, HttpServletRequest httpServletRequest){
        request.setUserId(tokenProvider.getUsernameFromToken(tokenProvider.getJwtToken(httpServletRequest)));
        Optional<FavoriteSong> existingFavorite = favoriteSongRepository.findByBrandAndNoAndUserId(request.getBrand(), request.getNo(), request.getUserId());

        //값이 있으면 삭제처리
        if (existingFavorite.isPresent()) {
            favoriteSongRepository.delete(existingFavorite.get());
            return ResponseEntity.ok(new FavoriteSongDto.Response(false));
        } else {
            //값이 없으면 저장
            favoriteSongRepository.save(request.toEntity());
            return ResponseEntity.ok(new FavoriteSongDto.Response(true));
        }
    }

    //request 생성
    private String getRequestUrl(SearchSongDto.Request request){
        String target = request.getUrlTarget();
        String url = "";
        if(target.equals("search")){
            url = searchUrl;
        }else if(target.equals("release")){
            url = releaseUrl;
        }

        //파라미터 붙이기
        url+=getMakeParam(request);

        return url;
    }

    private String getMakeParam(SearchSongDto.Request request){
        String result = "";

        //kumyoung / tj / dam / joysound
        if(StringUtils.hasText(request.getBrand().trim())){
            result+="?brand="+request.getBrand();
        }

        //인기차트일때 날짜 붙여주기
        if(request.getUrlTarget().equals("release")){
            result+="&release="+request.getSearchDate();
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
