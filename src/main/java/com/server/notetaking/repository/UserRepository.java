package com.server.notetaking.repository;

import com.server.notetaking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository <User, Serializable> {
    @Query(value = "SELECT * FROM user_info u WHERE u.username =:username and u.appId =:appId",
            nativeQuery = true)
    User findIfUsernameAndAppIdExists(@Param("username") String username, @Param("appId")String appId);


    @Query(value = "SELECT * FROM user_info u WHERE u.username = :username and u.appId = :appId",
            nativeQuery = true)
    Optional<User> findIfUsernameAndAppIdisPresent(@Param("username") String username, @Param("appId")String appId);


    @Query(value = "SELECT * FROM User_info u where u.refresh_token = :refreshToken and u.appId=:appId and u.refresh_token_expiry >= :today",
            nativeQuery = true)
    User findByAppIdAndRefreshToken(@Param("appId")String appId, @Param("refreshToken")String refreshToken,  @Param("today") Date today);
}
