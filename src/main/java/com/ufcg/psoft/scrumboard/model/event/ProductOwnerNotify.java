package com.ufcg.psoft.scrumboard.model.event;

import com.ufcg.psoft.scrumboard.interfaces.Event;
import com.ufcg.psoft.scrumboard.interfaces.UserStoryState;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;
import com.ufcg.psoft.scrumboard.model.state.Done;

public class ProductOwnerNotify extends ListenerUserStoryAdapter {

    public ProductOwnerNotify(User user) {
        super(user);
    }

    @Override
    public void changedState(Event<UserStory> event) {
        String state = event.getSource().getState();
        if(state.equals("DONE")) {
            User user = super.getUser();
            user.changedState(event);
            String msg = String.format("User: %s product owner está sendo notificado >> A user story com ID: %s " +
                            "foi para o estágio DONE!",
                    user.getId(), event.getSource().getID());
            System.out.println(msg);
        }
    }

}
