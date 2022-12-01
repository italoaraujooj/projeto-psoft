package com.ufcg.psoft.scrumboard.interfaces;

import com.ufcg.psoft.scrumboard.model.User;

public interface UserCrud extends Crud<String, User> {

    public User findOneByID(String id);

}
