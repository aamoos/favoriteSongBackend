package com.favoriteSongBackend.repository;

import com.favoriteSongBackend.entity.FavoriteSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteSongRepository extends JpaRepository<FavoriteSong, Long> {

    Optional<FavoriteSong> findByBrandAndNo(String brand, Long no);
}
