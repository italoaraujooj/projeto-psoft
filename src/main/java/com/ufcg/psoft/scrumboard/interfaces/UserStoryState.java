package com.ufcg.psoft.scrumboard.interfaces;

import com.ufcg.psoft.scrumboard.model.User;

public interface UserStoryState {
    
    public void addResponsible(User user);

    public void finishTask(String idTask);


}
