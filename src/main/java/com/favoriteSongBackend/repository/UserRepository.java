package com.favoriteSongBackend.repository;

import com.favoriteSongBackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    //소셜 id로 사용자 정보 조회
    Optional<Users> findBySocialId(String id);

    Optional<Users> findByUserId(String username);
}
