package com.meetup.meetup.dao;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.entity.Folder;
import com.meetup.meetup.entity.User;

import java.util.List;


public interface EventDao extends Dao<Event> {

    Event findByFolder(Folder folder);

    List<User> getParticipants(Event event);
}