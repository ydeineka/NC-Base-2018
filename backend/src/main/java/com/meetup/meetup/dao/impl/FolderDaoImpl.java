package com.meetup.meetup.dao.impl;

import com.meetup.meetup.dao.FolderDao;
import com.meetup.meetup.dao.rowMappers.EventRowMapper;
import com.meetup.meetup.dao.rowMappers.FolderRowMapper;
import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.DatabaseWorkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@PropertySource("classpath:sqlDao.properties")
public class FolderDaoImpl implements FolderDao {

    @Autowired
    private Environment env;

    @Autowired
    @Qualifier("jdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Event> getEvents(Folder folder) {
        List<Event> events = new ArrayList<>();

        try {
            events = jdbcTemplate.query(env.getProperty("folder.getEvents"),
                    new Object[]{folder.getFolderId()}, new EventRowMapper());
        } catch (DataAccessException e){
            System.out.println(e.getMessage());
        }

        return events;
    }

    @Override
    public List<Folder> getUserFolders(User user) {
        List<Folder> userFolders = new ArrayList<>();

        try{
            userFolders = jdbcTemplate.query(env.getProperty("folder.getUserFolders"),
                    new Object[]{user.getId()}, new FolderRowMapper());
        } catch (DataAccessException e){
            System.out.println(e.getMessage());
        }

        return userFolders;
    }

    @Override
    public Folder findById(int id) {
        Folder folder = null;

        try{
            folder = jdbcTemplate.queryForObject(
                    env.getProperty("folder.getById"),
                    new Object[]{id}, new FolderRowMapper()
            );
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        return folder;
    }

    @Override
    public Folder insert(Folder model) {

        int id;

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("FOLDER")
                .usingGeneratedKeyColumns("FOLDER_ID");

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("folder_id", model.getFolderId());
        parameters.put("name", model.getName());
        parameters.put("user_id", model.getUserId());

        try {
            id = simpleJdbcInsert.executeAndReturnKey(parameters).intValue();
            model.setFolderId(id);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }

        return model;
    }

    @Override
    public Folder update(Folder model) {
        try{
            jdbcTemplate.update(env.getProperty("folder.update"),
                    model.getName(), model.getFolderId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }

        return model;
    }

    @Override
    public Folder delete(Folder model) {
        try {
            jdbcTemplate.update(env.getProperty("folder.delete"), model.getFolderId());
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
            throw new DatabaseWorkException();
        }

        return model;
    }
}
