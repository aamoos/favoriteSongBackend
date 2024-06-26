package com.favoriteSongBackend.repository;

import com.favoriteSongBackend.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {

    Optional<Email> findFirstByUserIdOrderByCreatedDateDesc(String userId);

}
