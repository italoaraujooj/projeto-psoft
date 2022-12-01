package com.ufcg.psoft.scrumboard.model.event;

import com.ufcg.psoft.scrumboard.interfaces.Event;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;

public class ScrumMasterNotify extends ListenerUserStoryAdapter {

    public ScrumMasterNotify(User user) {
        super(user);
    }

    @Override
    public void changedDescription(Event<UserStory> event) {
        User user = super.getUser();
        user.changedDescription(event);
        String msg = String.format("User: %s scrum master está sendo notificado >> A user story com ID: %s " +
                        "teve sua descrição alterada para: %s.",
                user.getId(), event.getSource().getID(), event.getSource().getDescription());
        System.out.println(msg);
    }

    @Override
    public void changedState(Event<UserStory> event) {
        User user = super.getUser();
        user.changedState(event);
        String msg = String.format("User: %s scrum master está sendo notificado >> A user story com ID: %s " +
                        "foi para o estágio %s!",
                user.getId(), event.getSource().getID(), event.getSource().getState());
        System.out.println(msg);
    }

    @Override
    public void finishedTask(Event<UserStory> event) {
        User user = super.getUser();
        user.finishedTask(event);
        String msg = String.format("User: %s scrum master está sendo notificado >> A task ID: %s " +
                        "foi realizada!.",
                user.getId(), event.getSource().getIdLastTaskFinished());
        System.out.println(msg);
    }

}
