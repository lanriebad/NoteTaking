package com.server.notetaking.repository;

import com.server.notetaking.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface RoleRepository extends JpaRepository<Role, Serializable> {

    @Query("Select r from Role r where r.id =:id ")
    Role findById(@Param("id")Long id);
}
