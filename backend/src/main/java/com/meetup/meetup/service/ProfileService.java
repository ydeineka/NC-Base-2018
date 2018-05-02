package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.runtime.EntityNotFoundException;
import com.meetup.meetup.security.AuthenticationFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.List;

import static com.meetup.meetup.Keys.Key.EXCEPTION_ENTITY_NOT_FOUND;

@Service
@PropertySource("classpath:strings.properties")
public class ProfileService {

    private static Logger log = LoggerFactory.getLogger(ProfileService.class);

    private final UserDao userDao;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public ProfileService(UserDao userDao, AuthenticationFacade authenticationFacade) {
        this.userDao = userDao;
        this.authenticationFacade = authenticationFacade;
    }

    @Autowired
    private Environment env;

    private static Logger log = LoggerFactory.getLogger(ProfileService.class);

    public User getUserByLogin(String login) {
        User user = userDao.findByLogin(login);

        if(user == null) {
            log.error("User was not found by userLogin '{}'", login);
            throw new EntityNotFoundException(String.format(env.getProperty(EXCEPTION_ENTITY_NOT_FOUND),"User", "userLogin", login));
        }

        log.debug("Found user '{}'", user.toString());

        return user;
    }


    // TODO for further improvements of user search
    /*public List<User> searchUsers(String login, String name, String surname, Integer limit) {

        log.debug("Trying to search users by features login '{}', name '{}', surname '{}' and limit '{}'",
                login, name, surname, limit);

        List<User> users = userDao.findByParams(login, name, surname, limit);

        if (users == null) {
            log.error("Cannot get user from database");
            throw new DatabaseWorkException();
        }

        log.debug("Found users '{}'", users);

        return users;
    }*/

    public User updateUser(User newUser) {
        return userDao.update(newUser);
    }

    public List<User> getFriends(String login) {

        User user = userDao.findByLogin(login);

        log.debug("User for finding friends '{}'", user.toString());

        return userDao.getFriends(user.getId());
    }

    public List<User> getFriendsRequests() {
        User user = authenticationFacade.getAuthentication();

        log.debug("Authenticated user '{}'", user.toString());

        return userDao.getFriendsRequests(user.getId());
    }

    public boolean addFriend(String friendLogin){
        User user = authenticationFacade.getAuthentication();

        log.debug("Authenticated user '{}'", user.toString());

        User friend = userDao.findByLogin(friendLogin);

        log.debug("Friend found '{}'", friend.toString());

        return friend != null && userDao.addFriend(user.getId(), friend.getId());
    }

    public void confirmFriend(int friendId){
        User user = authenticationFacade.getAuthentication();

        log.debug("Authenticated user '{}'", user.toString());

        if(userDao.confirmFriend(user.getId(), friendId) == user.getId()){
            log.debug("Friend successfully confirmed ");
        }
    }

    public void deleteFriend(int friendId){
        User user = authenticationFacade.getAuthentication();

        log.debug("Authenticated user '{}'", user.toString());

        if(userDao.deleteFriend(user.getId(), friendId)== user.getId()){
            log.debug("Friend successfully deleted ");
        }
    }

    public List<User> getUnknownUsers(String userName){
        User user = authenticationFacade.getAuthentication();
        return userDao.getNotFriends(user.getId(),userName);
    }
}