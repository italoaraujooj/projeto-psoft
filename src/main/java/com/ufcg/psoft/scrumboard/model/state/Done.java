package com.ufcg.psoft.scrumboard.model.state;

import com.ufcg.psoft.scrumboard.exception.WithoutPermissionException;
import com.ufcg.psoft.scrumboard.interfaces.UserStoryState;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;

public class Done implements UserStoryState {

    UserStory userStory;

    public Done(UserStory userStory) {
        this.userStory = userStory;
    }

    @Override
    public void addResponsible(User user) {
        throw new WithoutPermissionException("Invalid operation! The user story is already completed.");
    }

    @Override
    public void finishTask(String idTask) {
        throw new WithoutPermissionException("Invalid operation! The user story is already completed.");
    }

    @Override
    public String toString() {
        return "DONE";
    }
}
