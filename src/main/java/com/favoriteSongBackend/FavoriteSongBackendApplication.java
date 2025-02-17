package com.favoriteSongBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FavoriteSongBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FavoriteSongBackendApplication.class, args);

		// 환경변수 값 확인
		System.out.println("KAKAO_CLIENT_ID: " + System.getenv("KAKAO_CLIENT_ID"));
		System.out.println("KAKAO_CLIENT_SECRET: " + System.getenv("KAKAO_CLIENT_SECRET"));
		System.out.println("KAKAO_REDIRECT_URI: " + System.getenv("KAKAO_REDIRECT_URI"));

		System.out.println("NAVER_CLIENT_ID: " + System.getenv("NAVER_CLIENT_ID"));
		System.out.println("NAVER_CLIENT_SECRET: " + System.getenv("NAVER_CLIENT_SECRET"));
		System.out.println("NAVER_REDIRECT_URI: " + System.getenv("NAVER_REDIRECT_URI"));

		System.out.println("GOOGLE_CLIENT_ID: " + System.getenv("GOOGLE_CLIENT_ID"));
		System.out.println("GOOGLE_CLIENT_SECRET: " + System.getenv("GOOGLE_CLIENT_SECRET"));
		System.out.println("GOOGLE_REDIRECT_URI: " + System.getenv("GOOGLE_REDIRECT_URI"));

	}

}
