package com.ufcg.psoft.scrumboard.model.event;

import com.ufcg.psoft.scrumboard.interfaces.Event;
import com.ufcg.psoft.scrumboard.interfaces.Listener;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;

public abstract class ListenerUserStoryAdapter implements Listener<Event<UserStory>> {

    User user;

    public ListenerUserStoryAdapter(User user) {
        this.user = user;
    }

    protected User getUser() {
        return this.user;
    }

    @Override
    public void changedDescription(Event<UserStory> event) {
        // TODO Do nothing
    }

    @Override
    public void changedState(Event<UserStory> event) {
        // TODO Do nothing
    }

    @Override
    public void finishedTask(Event<UserStory> event) {
        // TODO Do nothing
    }

}
