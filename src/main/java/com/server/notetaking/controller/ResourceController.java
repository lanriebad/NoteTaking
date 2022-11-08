package com.server.notetaking.controller;

import com.server.notetaking.dto.*;
import com.server.notetaking.model.ApplicationInfo;
import com.server.notetaking.model.Label;
import com.server.notetaking.model.Note;
import com.server.notetaking.service.ResourceService;
import com.server.notetaking.utils.JwtTokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("api/secure")
@RolesAllowed({"user"})
@CrossOrigin(origins = "*")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;


    @RolesAllowed("user")
    @PostMapping("/note")
    DefaultServiceResponse addNote(@RequestHeader(name = "Authorization", required = true) String token, @RequestHeader(name = "x-app-id") String appId, @RequestBody LabelRequest request) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        Optional<ApplicationInfo> info = resourceService.findByApplicationId(appId);
        if (info.isPresent()) {
            request.setUsername(JwtTokenUtil.getUsernameFromToken(token.split(" ")[1]));
            response = resourceService.addNote(request, appId);
        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getDefaultMessage());
        }
        return response;
    }


    @RolesAllowed("user")
    @GetMapping("/note/{id}")
    DefaultServiceResponse getNotesById(@RequestHeader(name = "Authorization", required = true) String token, @RequestHeader(name = "x-app-id") String appId,@PathVariable Long id) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        String username = JwtTokenUtil.getUsernameFromToken(token.split(" ")[1]);
        Optional<Note> noteOptional = resourceService.findNoteByIdAndAndUsernameAndAppId(id,username, appId);
        if (noteOptional.isPresent()) {
            response = resourceService.getNotesById(noteOptional);
        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getDefaultMessage());
        }
        return response;
    }


    @RolesAllowed("user")
    @GetMapping("/label/{id}")
    DefaultServiceResponse getLabelById(@RequestHeader(name = "Authorization", required = true) String token, @RequestHeader(name = "x-app-id") String appId,@PathVariable Long id) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        Optional<Label> noteOptional = resourceService.findLabelById(id);
        if (noteOptional.isPresent()) {
            response = resourceService.getLabelById(noteOptional.get().getId());
        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getDefaultMessage());
        }
        return response;
    }


    @RolesAllowed("user")
    @PutMapping("/edit/note")
    DefaultServiceResponse editNote(@RequestHeader(name = "Authorization", required = true) String token, @RequestHeader(name = "x-app-id") String appId, @RequestBody NoteRequest request) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        String username = JwtTokenUtil.getUsernameFromToken(token.split(" ")[1]);
        Optional<Note> noteOptional = resourceService.findNoteByIdAndUsernameAndAppId(request.getId(),username, appId);
        if (noteOptional.isPresent()) {
            response = resourceService.editNote(noteOptional,request);
        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getDefaultMessage());
        }
        return response;
    }


    @RolesAllowed("user")
    @GetMapping("/all/notes")
    DefaultServiceResponse showAllNotes(@RequestHeader(name = "Authorization", required = true) String token, @RequestHeader(name = "x-app-id") String appId) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        String username = JwtTokenUtil.getUsernameFromToken(token.split(" ")[1]);
        List<NoteLabelResponse> noteList = resourceService.findAllNotesAndLabels(username, appId);
        if (!noteList.isEmpty()) {
            response.setResponseCode(ServiceResponse.ResponseCode.SUCCESSFUL.getCode());
            response.setResponseData(noteList);
            response.setResponseMsg(ServiceResponse.ResponseCode.SUCCESSFUL.getDefaultMessage());
        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getDefaultMessage());
        }
        return response;
    }

    @RolesAllowed("user")
    @GetMapping("/all/labels")
    DefaultServiceResponse showAllLabels(@RequestHeader(name = "Authorization", required = true) String token, @RequestHeader(name = "x-app-id") String appId) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        String username = JwtTokenUtil.getUsernameFromToken(token.split(" ")[1]);
        List<LabelResponse> noteList = resourceService.findAllLabels(username, appId);
        if (!noteList.isEmpty()) {
            response.setResponseCode(ServiceResponse.ResponseCode.SUCCESSFUL.getCode());
            response.setResponseData(noteList);
            response.setResponseMsg(ServiceResponse.ResponseCode.SUCCESSFUL.getDefaultMessage());
        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getDefaultMessage());
        }
        return response;
    }


    @RequestMapping(value = "label/autocomplete")
    public List<Map<String, Object>> getLabelAutoComplete(@RequestHeader(name = "Authorization", required = true) String token,@RequestHeader(name = "x-app-id") String appId,@RequestParam("term") String term) {
        String username = JwtTokenUtil.getUsernameFromToken(token.split(" ")[1]);
        return resourceService.getLabelAutoComplete(term,username,appId);
    }

    @PostMapping("/search")
    public List searchByParams(@RequestHeader(name = "Authorization", required = true) String token,@RequestHeader(name = "x-app-id") String appId,@RequestBody SearchDataFilter filter) {
        List<SearchResponse> searchResposnes = new ArrayList<>();
        if (StringUtils.isNotBlank(appId)) {
            String username = JwtTokenUtil.getUsernameFromToken(token.split(" ")[1]);
            filter.setAppId(appId);
            searchResposnes = resourceService.searchByParams(filter,username);
        }
        return searchResposnes;
    }


    @DeleteMapping(value = "delete/note/{id}")
    public DefaultServiceResponse deleteNote(@RequestHeader(name = "Authorization", required = true) String token,@RequestHeader(name = "x-app-id") String appId,@PathVariable Long id) {
        DefaultServiceResponse response = new DefaultServiceResponse();
        String username = JwtTokenUtil.getUsernameFromToken(token.split(" ")[1]);
        Optional<Note> deleteNoteOptional = resourceService.findNoteByIdAndAndUsernameAndAppId(id,username, appId);
        if (deleteNoteOptional.isPresent()) {
            response =resourceService.deleteNote(deleteNoteOptional,username,appId);
        } else {
            response.setResponseCode(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getCode());
            response.setResponseMsg(ServiceResponse.ResponseCode.REQUEST_NOT_FOUND.getDefaultMessage());
        }
        return response;
    }
}
