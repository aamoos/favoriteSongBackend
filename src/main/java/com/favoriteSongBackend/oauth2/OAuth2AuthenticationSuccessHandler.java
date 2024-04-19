package com.favoriteSongBackend.oauth2;


import com.favoriteSongBackend.entity.Users;
import com.favoriteSongBackend.jwt.TokenProvider;
import com.favoriteSongBackend.repository.UserRepository;
import com.favoriteSongBackend.utils.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.favoriteSongBackend.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

/**
 * packageName    : com.hellzzang.oauth2
 * fileName       : OAuth2AuthenticationSuccessHandler
 * author         : 김재성
 * date           : 2023-06-21
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-06-21        김재성       최초 생성
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    //    private final JwtTokenProvider tokenProvider;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());

        DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
        Map map = defaultOAuth2User.getAttributes();

        String socialId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId();
        String id = "";
        if("kakao".equals(socialId)){
            id = String.valueOf(map.get("id"));
        }else if("naver".equals(socialId)){
            LinkedHashMap naverMap = (LinkedHashMap) map.get("response");
            id = String.valueOf(naverMap.get("id"));
        }else if("google".equals(socialId)){
            id = String.valueOf(map.get("sub"));
        }

        Optional<Users> optionalUser = userRepository.findBySocialId(id);
        String token = "";
        String refreshToken = "";

        optionalUser.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Authentication auth = (Authentication) optionalUser.get();
        token = tokenProvider.createToken(auth);
        refreshToken = tokenProvider.createRefreshToken(auth);

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", token)
                .queryParam("refreshToken", refreshToken)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
