package com.ufcg.psoft.scrumboard.model.state;


import com.ufcg.psoft.scrumboard.exception.WithoutPermissionException;
import com.ufcg.psoft.scrumboard.interfaces.UserStoryState;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;

import java.util.Map;

public class ToDo implements UserStoryState {

    UserStory userStory;

    public ToDo(UserStory userStory) {
        this.userStory = userStory;
    }

    @Override
    public void addResponsible(User user) {
        this.userStory.getUsers().put(user.getId(), user);
        this.userStory.setState(new InProgress(this.userStory));
    }

    @Override
    public void finishTask(String idTask) {
        throw new WithoutPermissionException("It is not possible to end a task in the to do state." +
                " Assign a responsible user to user story!");
    }

    @Override
    public String toString() {
        return "TO_DO";
    }

}
