package com.meetup.meetup.service;

import com.meetup.meetup.dao.UserDao;
import com.meetup.meetup.entity.User;
import com.meetup.meetup.exception.ProfileNotFoundException;
import com.meetup.meetup.service.vm.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtService jwtService;

    public Profile getProfile(String login) {

        final User user = userDao.findByLogin(login);

        if (user != null) {

            return new Profile(user);
        }

        throw new ProfileNotFoundException(login);
    }

    // TODO: 20.04.2018 logic with -1
    public boolean updateProfile(Profile updatedProfile) {

        User updatedUser = userDao.findById(updatedProfile.getId());

        if (updatedUser != null) {

            User newUser = updatedProfile.getUser(updatedUser);

            return userDao.update(newUser) != -1;

        } else {
            throw new ProfileNotFoundException(updatedProfile.getId());
        }
    }


    public List<Profile> getFriends(int id) {

        List<Integer> listFriendsIds = userDao.getFriendsIds(id);

        List<Profile> listFriendsProfiles = new ArrayList<>();

        listFriendsIds.forEach((friendId) -> listFriendsProfiles.add(new Profile(userDao.findById(friendId))));

        return listFriendsProfiles;
    }

    public Profile getProfileFromToken(String token) {

        try {

            return jwtService.verify(token);

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}