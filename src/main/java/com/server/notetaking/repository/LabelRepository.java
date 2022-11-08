package com.server.notetaking.repository;

import com.server.notetaking.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface LabelRepository extends JpaRepository<Label, Serializable> {

    Optional<Label> findByName(String name);

    Label findLabelById(Long id);


    @Query(value = "SELECT * FROM Label u WHERE u.username =:username and u.appId=:appId",
            nativeQuery = true)
    List<Label> findAllByUsernameAndAppId(@Param("username")String username,@Param("appId")String appId);

}
