package com.server.notetaking.repository;

import com.server.notetaking.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Serializable> {

    Optional<Note> findNoteByUsernameAndAppId(String username, String appId);

    Optional<Note> findNoteByIdAndUsernameAndAppId(Long id,String username, String appId);


    Optional<Note> findNoteByIdAndAndUsernameAndAppId(Long id,String username, String appId);

    @Query(value = "SELECT * FROM note u WHERE u.label_id =:id",
            nativeQuery = true)
    List<Note> findNotesByLabel_Id(@Param("id")Long id);

    List<Note> findNotesByUsernameAndAppId(String username,String appId);


    @Transactional
    @Modifying
    @Query("DELETE FROM Note where id =:id and username=:username and appId=:appId")
    void deleteNoteByIdAndUsernameAndAppId(@Param("id") Long id, @Param("username") String username,@Param("appId") String appId);
}
