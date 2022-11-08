package com.server.notetaking.dao;


import com.server.notetaking.dto.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class JdbcDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<Map<String, Object>> getLabelAutoComplete(String term, String username, String appId) {
        String termSql = StringUtils.defaultIfBlank(term, StringUtils.EMPTY);
        termSql = "%" + term.toLowerCase().trim() + "%";
        String sql = "select a.label_id as key,(a.name) as label from Label  a join note b on a.label_id=b.label_id where lower(a.name) like ? and b.username=? and b.app_id=?";
        Object[] args = new Object[3];
        args[0] = termSql;
        args[1] = username;
        args[2] = appId;
        return jdbcTemplate.queryForList(sql, args);

    }

    public List<SearchResponse> searchInformationByParams(SearchDataFilter filter, String username) {
        List<SearchResponse> searchResponseList = new ArrayList<>();
        String whereClause = StringUtils.EMPTY;
        MapSqlParameterSource param = new MapSqlParameterSource();
        if (StringUtils.isNotBlank(filter.getSearchInfo())) {
            param.addValue("searchInfo", "%" + filter.getSearchInfo().toLowerCase().trim() + "%");
            whereClause = " and lower(name) LIKE :searchInfo or lower(title) LIKE :searchInfo or lower(content) LIKE :searchInfo";
        }
        String sql =String.format("select a.name as name,a.label_id as labelid,b.id as noteid ,b.title as title ,b.content as content  from Label  a join note b on a.label_id=b.label_id where b.username='%s' and b.app_id='%s' " + whereClause + " ",username,filter.getAppId());
        searchResponseList =namedParameterJdbcTemplate.query(sql, param, new RowMapper<SearchResponse>() {
            @Override
            public SearchResponse mapRow(ResultSet rs, int arg1) throws SQLException {
                SearchResponse response = new SearchResponse();
                response.setContent(rs.getString("content"));
                response.setTitle(rs.getString("title"));
                response.setLabelName(rs.getString("name"));
                response.setLabelId(rs.getLong("labelid"));
                response.setNoteId(rs.getLong("noteid"));

                return response;
            }
        });
        return searchResponseList;
    }


    public List<NoteLabelResponse> findAllNotesAndLabels(String username,String appId){
        List<NoteLabelResponse> noteResponses= new ArrayList<>();
        try {
            String sql = String.format("select a.name as labelname,a.label_id as labelId,b.id as noteId,b.title as title ,b.content as content  from Label  a join note b on a.label_id=b.label_id where b.username='%s' and b.app_id='%s'", username, appId);
            noteResponses = jdbcTemplate.query(sql, new RowMapper<NoteLabelResponse>() {
                public NoteLabelResponse mapRow(ResultSet rs, int arg1) throws SQLException {
                    NoteLabelResponse response = new NoteLabelResponse();
                    response.setContent(rs.getString("content"));
                    response.setTitle(rs.getString("title"));
                    response.setLabelName(rs.getString("labelname"));
                    response.setLabelId((rs.getLong("labelId")));
                    response.setNoteId((rs.getLong("noteId")));
                    return response;
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
        return noteResponses;

        }



    public List<LabelResponse> findAllByUsernameAndAppId(String username,String appId) {
        List<LabelResponse> responseResponseList = new ArrayList<>();
        try {
            String sql = String.format(" select distinct(a.label_id) as id,a.name as name from Label a join note b on a.label_id=b.label_id where b.username='%s' and b.app_id='%s'", username, appId);
            responseResponseList = namedParameterJdbcTemplate.query(sql, new RowMapper<LabelResponse>() {
                @Override
                public LabelResponse mapRow(ResultSet rs, int arg1) throws SQLException {
                    LabelResponse response = new LabelResponse();
                    response.setId(rs.getLong("id"));
                    response.setName(rs.getString("name"));
                    return response;
                }
            });
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return responseResponseList;
    }


}
