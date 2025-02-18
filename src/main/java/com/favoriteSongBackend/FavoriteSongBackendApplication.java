package com.favoriteSongBackend;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FavoriteSongBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FavoriteSongBackendApplication.class, args);

		// .env 파일 로드
		Dotenv dotenv = Dotenv.load();

		// 환경변수 값 확인
		System.out.println("KAKAO_CLIENT_ID: " + dotenv.get("KAKAO_CLIENT_ID"));
		System.out.println("KAKAO_CLIENT_SECRET: " + dotenv.get("KAKAO_CLIENT_SECRET"));
		System.out.println("KAKAO_REDIRECT_URI: " + dotenv.get("KAKAO_REDIRECT_URI"));

		System.out.println("NAVER_CLIENT_ID: " + dotenv.get("NAVER_CLIENT_ID"));
		System.out.println("NAVER_CLIENT_SECRET: " + dotenv.get("NAVER_CLIENT_SECRET"));
		System.out.println("NAVER_REDIRECT_URI: " + dotenv.get("NAVER_REDIRECT_URI"));

		System.out.println("GOOGLE_CLIENT_ID: " + dotenv.get("GOOGLE_CLIENT_ID"));
		System.out.println("GOOGLE_CLIENT_SECRET: " + dotenv.get("GOOGLE_CLIENT_SECRET"));
		System.out.println("GOOGLE_REDIRECT_URI: " + dotenv.get("GOOGLE_REDIRECT_URI"));

	}

}
