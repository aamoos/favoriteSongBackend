package com.favoriteSongBackend.repository;

import com.favoriteSongBackend.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByUserId(String userId);

    Optional<Users> findByUserIdAndUserName(String userId, String userName);

}
