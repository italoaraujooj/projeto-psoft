package com.ufcg.psoft.scrumboard.repository;

import com.ufcg.psoft.scrumboard.interfaces.UserCrud;
import com.ufcg.psoft.scrumboard.model.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepository implements UserCrud {

    private Map<String, User> users;

    public UserRepository() { this.users = new HashMap<>(); }

    @Override
    public String create(User user) {
        this.users.put(user.getId(), user);
        return String.format("User: %s, criada.", user.getId());
    }

    @Override
    public String change(String id, User user) {
        this.users.replace(id, user);
        return String.format("User: %s, modificada.", id);
    }

    @Override
    public String delete(User user) {
        String id = user.getId();;
        this.users.remove(id);
        return String.format("User: %s, removida.", id);
    }

    @Override
    public Collection<User> listAll() {
        return this.users.values();
    }

    @Override
    public User findOneByID(String id) {
        return this.users.get(id);
    }

}
