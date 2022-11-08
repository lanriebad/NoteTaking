package com.server.notetaking.service;

import com.server.notetaking.dao.JdbcDao;
import com.server.notetaking.dto.*;
import com.server.notetaking.model.ApplicationInfo;
import com.server.notetaking.model.Label;
import com.server.notetaking.model.Note;
import com.server.notetaking.repository.ApplicationInfoRepository;
import com.server.notetaking.repository.NoteRepository;
import com.server.notetaking.repository.LabelRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

import static javafx.scene.input.KeyCode.L;

@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    ApplicationInfoRepository applicationInfoRepository;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private JdbcDao jdbcDao;

    @Override
    public Optional<ApplicationInfo> findByApplicationId(String appId) {
        return applicationInfoRepository.findApplicationInfoByApplicationId(appId);
    }

    @Override
    public DefaultServiceResponse addNote(LabelRequest request, String appId) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        response.setResponseCode(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getCode());
        response.setResponseMsg(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getDefaultMessage());
        Optional<Label> optionalLabel = labelRepository.findByName(request.getName());
        if (optionalLabel.isPresent()) {
            Label labelPresent = optionalLabel.get();
            labelPresent.setName(optionalLabel.get().getName());
            if (!CollectionUtils.isEmpty(request.getNotes())) {
                addLabelToNote(request, appId, labelPresent);
            }
            extractedResponse(labelPresent, response);
        } else {
            Label newLabel = new Label();
            if(StringUtils.isNotEmpty(request.getName())) {
                newLabel.setName(request.getName());
                if (!CollectionUtils.isEmpty(request.getNotes())) {
                    addLabelToNote(request, appId, newLabel);
                    extractedResponse(newLabel, response);
                }
            }

        }
        return response;
    }

    private void addLabelToNote(LabelRequest request, String appId, Label labelPresent) {
        List<Note> noteList = request.getNotes().stream().map(nt -> {
            Note note = new Note();
            note.setTitle(nt.getTitle());
            note.setContent(nt.getContent());
            note.setAppId(appId);
            note.setUsername(request.getUsername());
            note.setLabel(labelPresent);
            return note;
        }).collect(Collectors.toList());
        labelRepository.save(labelPresent);
        noteRepository.saveAll(noteList);


    }

    @Override
    public Optional<Note> findNoteByUsernameAndAppId(String username, String appId) {
        return noteRepository.findNoteByUsernameAndAppId(username, appId);
    }

    @Override
    public DefaultServiceResponse getNotesById(Optional<Note> notePresent) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        ServiceResponse.ResponseCode responseCode = ServiceResponse.ResponseCode.SUCCESSFUL;
        LabelNoteResponse note = new LabelNoteResponse();
        note.setId(notePresent.get().getId());
        note.setContent(notePresent.get().getContent());
        note.setTitle(notePresent.get().getTitle());
        note.setLabel(labelRepository.findLabelById(notePresent.get().getLabel().getId()));
        response.setResponseCode(responseCode.SUCCESSFUL.getCode());
        response.setResponseData(Collections.singletonList(note));
        response.setResponseMsg(responseCode.SUCCESSFUL.getDefaultMessage());
        return response;
    }

    @Override
    public DefaultServiceResponse editNote(Optional<Note> noteOptional, NoteRequest
            request) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        ServiceResponse.ResponseCode responseCode = ServiceResponse.ResponseCode.SUCCESSFUL;
        Note notePresent = noteOptional.get();
        notePresent.setTitle(request.getTitle());
        notePresent.setContent(request.getContent());
        noteRepository.save(notePresent);
        response.setResponseCode(responseCode.SUCCESSFUL.getCode());
        response.setResponseData(Collections.singletonList(notePresent));
        response.setResponseMsg(responseCode.SUCCESSFUL.getDefaultMessage());
        return response;
    }

    @Override
    public List<Map<String, Object>> getLabelAutoComplete(String term, String username, String appId) {
        return jdbcDao.getLabelAutoComplete(term, username, appId);
    }

    @Override
    public Optional<Label> findLabelById(Long id) {
        return labelRepository.findById(id);
    }

    @Override
    public DefaultServiceResponse getLabelById(Long id) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        response.setResponseCode(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getCode());
        response.setResponseMsg(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getDefaultMessage());        Optional<Label> labelOptional = findLabelById(id);
        if (labelOptional.isPresent()) {
            Label label = new Label();
            label.setName(labelOptional.get().getName());
            label.setId(labelOptional.get().getId());
            List<Note> note = noteRepository.findNotesByLabel_Id(labelOptional.get().getId());
            label.setNotes(note);
            response.setResponseCode(ServiceResponse.ResponseCode.SUCCESSFUL.getCode());
            response.setResponseData(Collections.singletonList(label));
            response.setResponseMsg(ServiceResponse.ResponseCode.SUCCESSFUL.getDefaultMessage());
        }
        return response;
    }

    @Override
    public List<SearchResponse> searchByParams(SearchDataFilter filter, String username) {
        return jdbcDao.searchInformationByParams(filter, username);
    }

    @Override
    public DefaultServiceResponse deleteNote(Optional<Note> deleteNoteOptional, String username, String appId) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        Note note = deleteNoteOptional.get();
        noteRepository.deleteNoteByIdAndUsernameAndAppId(note.getId(), username, appId);
        response.setResponseCode(ServiceResponse.ResponseCode.SUCCESSFUL.getCode());
        response.setResponseMsg(ServiceResponse.ResponseCode.SUCCESSFUL.getDefaultMessage());
        return response;
    }

    @Override
    public Optional<Note> findNoteByIdAndUsernameAndAppId(Long id, String username, String appId) {
        return noteRepository.findNoteByIdAndUsernameAndAppId(id,username,appId);
    }

    @Override
    public Optional<Note> findNoteByIdAndAndUsernameAndAppId(Long id, String username, String appId) {
        return noteRepository.findNoteByIdAndAndUsernameAndAppId(id,username,appId);
    }

    @Override
    public List<Note> findAllNotesByUsernameAndAppId(String username, String appId) {
        return noteRepository.findNotesByUsernameAndAppId(username,appId);
    }

    @Override
    public List<NoteLabelResponse> findAllNotesAndLabels(String username, String appId) {
        return jdbcDao.findAllNotesAndLabels(username,appId);
    }

    @Override
    public List<LabelResponse> findAllLabels(String username, String appId) {
        List<LabelResponse> labelLists = jdbcDao.findAllByUsernameAndAppId(username,appId);
        return labelLists;
    }


    private void extractedResponse(Label savedLabel, DefaultServiceResponse response) {
        Label label = new Label();
        label.setId(savedLabel.getId());
        label.setName(savedLabel.getName());
        label.setNotes(noteRepository.findNotesByLabel_Id(savedLabel.getId()));
        response.setResponseCode(ServiceResponse.ResponseCode.SUCCESSFUL.getCode());
        response.setResponseData(Collections.singletonList(label));
        response.setResponseMsg(ServiceResponse.ResponseCode.SUCCESSFUL.getDefaultMessage());

    }


}
