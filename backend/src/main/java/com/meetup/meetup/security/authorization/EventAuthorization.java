package com.meetup.meetup.security.authorization;

import com.meetup.meetup.entity.Event;
import com.meetup.meetup.security.AuthenticationFacade;
import org.springframework.stereotype.Component;

@Component
public class EventAuthorization extends AbstractAuthorization {

    public EventAuthorization(AuthenticationFacade authenticationFacade) {
        super(authenticationFacade);
    }

    public boolean isEventCorrect(int userId, Event event) {
        if (event == null) {
            return false;
        }

        return isUserCorrect(userId) && userId == event.getOwnerId();
    }

    public boolean isEventCorrect(int userId, int eventId, Event event) {
        return isEventCorrect(userId, event) && eventId == event.getEventId();
    }

}
