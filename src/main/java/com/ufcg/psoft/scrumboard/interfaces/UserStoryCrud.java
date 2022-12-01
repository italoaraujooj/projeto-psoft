package com.ufcg.psoft.scrumboard.interfaces;

import com.ufcg.psoft.scrumboard.model.UserStory;

public interface UserStoryCrud extends Crud<String, UserStory> {
    public UserStory findOne(String id);
}
