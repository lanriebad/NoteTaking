package com.server.notetaking.repository;

import com.server.notetaking.model.ApplicationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository

public interface ApplicationInfoRepository extends JpaRepository<ApplicationInfo,Serializable> {

    @Query("Select u from ApplicationInfo u where  u.appId=:appId ")
    ApplicationInfo findApplicationInfoByAppId(@Param("appId") String appId);


    @Query("Select u from ApplicationInfo u where  u.appId=:appId ")
    Optional<ApplicationInfo> findApplicationInfoByApplicationId(@Param("appId") String appId);


    @Query("Select u from ApplicationInfo u where  u.appId=:appId ")
    ApplicationInfo findByApplicationId(@Param("appId") String appId);
}
