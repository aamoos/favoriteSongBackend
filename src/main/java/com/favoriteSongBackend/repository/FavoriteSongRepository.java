package com.favoriteSongBackend.repository;

import com.favoriteSongBackend.entity.FavoriteSong;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteSongRepository extends JpaRepository<FavoriteSong, Long> {

    Optional<FavoriteSong> findByBrandAndNo(String brand, Long no);

    List<FavoriteSong> findByUserId(String userId);
}
