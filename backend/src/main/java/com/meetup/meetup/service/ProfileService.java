package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.security.AuthenticationFacade;
import com.meetup.meetup.service.vm.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationFacade authenticationFacade;

    public User getUser(int id) {
        return userDao.findById(id);
    }

    // TODO: 20.04.2018 logic with -1
    public boolean updateUser(User newUser) {
        User updatedUser = userDao.findById(newUser.getId()).setState(newUser);
        if(userDao.update(updatedUser) != -1){
            return true;
        }
        return false;
    }

    public List<Friend> getFriends() {
        User user = authenticationFacade.getAuthentication();
        // TODO: 21.04.2018 getFriendsIds(id) must to return friends list
//        List<Friend> listFriends = userDao.getFriendsIds(user.getId());
        return null;
    }
}