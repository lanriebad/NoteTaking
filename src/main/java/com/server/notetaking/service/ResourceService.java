package com.server.notetaking.service;

import com.server.notetaking.dto.*;
import com.server.notetaking.model.ApplicationInfo;
import com.server.notetaking.model.Label;
import com.server.notetaking.model.Note;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ResourceService {
    Optional<ApplicationInfo> findByApplicationId(String appId);

    DefaultServiceResponse addNote(LabelRequest request, String appId);

    Optional<Note> findNoteByUsernameAndAppId(String username, String appId);

    DefaultServiceResponse getNotesById(Optional<Note> notePresent);

    DefaultServiceResponse editNote(Optional<Note> noteOptional,NoteRequest noteRequest);

    List<Map<String, Object>> getLabelAutoComplete(String term, String username, String appId);

    Optional<Label> findLabelById(Long id);

    DefaultServiceResponse getLabelById(Long id);

    List<SearchResponse> searchByParams(SearchDataFilter filter, String username);


    DefaultServiceResponse deleteNote(Optional<Note > deleteNoteOptional,String username,String appId);


    Optional<Note> findNoteByIdAndUsernameAndAppId(Long id, String username, String appId);

    Optional<Note> findNoteByIdAndAndUsernameAndAppId(Long id, String username, String appId);

    List<Note> findAllNotesByUsernameAndAppId(String username, String appId);

    List<NoteLabelResponse> findAllNotesAndLabels(String username, String appId);

    List<LabelResponse> findAllLabels(String username, String appId);
}
