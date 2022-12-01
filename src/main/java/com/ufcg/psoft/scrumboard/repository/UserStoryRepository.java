package com.ufcg.psoft.scrumboard.repository;

import com.ufcg.psoft.scrumboard.interfaces.UserStoryCrud;
import com.ufcg.psoft.scrumboard.model.Task;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserStoryRepository implements UserStoryCrud {

    private Map<String, UserStory> userStories;

    public UserStoryRepository() { this.userStories = new HashMap<>(); }


    @Override
    public String create(UserStory userStory) {
        this.userStories.put(userStory.getID(), userStory);
        return String.format("UserStorie: %s, criada.", userStory.getID());
    }

    @Override
    public String change(String id, UserStory userStory) {
        this.userStories.replace(id, userStory);
        return String.format("UserStorie: %s, modificada.", id);
    }

    @Override
    public String delete(UserStory userStory) {
        String id = userStory.getID();
        this.userStories.remove(id);
        return String.format("UserStorie: %s, removida.", id);
    }

    public String delete(String id) {
        this.userStories.remove(id);
        return String.format("UserStorie: %s, removida.", id);
    }

    @Override
    public Collection<UserStory> listAll() {
        return this.userStories.values();
    }

    @Override
    public UserStory findOne(String id) {
        return this.userStories.get(id);
    }

}
