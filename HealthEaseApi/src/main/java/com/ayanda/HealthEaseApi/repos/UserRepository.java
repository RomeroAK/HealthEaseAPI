package com.ayanda.HealthEaseApi.repos;


import com.ayanda.HealthEaseApi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserNameAndPassword(String username, String password);
    boolean existsByUserName(String username);
    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.profileCompleted = true WHERE u.id = :userId")
    void profileCompleteToTrue(@Param("userId") Long userId);


}
