package com.ufcg.psoft.scrumboard.service;

import com.ufcg.psoft.scrumboard.dto.UserDTO;
import com.ufcg.psoft.scrumboard.exception.UserNotFoundException;
import com.ufcg.psoft.scrumboard.model.*;
import com.ufcg.psoft.scrumboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRep;

    public List<User> listUsers(){
        if (this.userRep.listAll().isEmpty()) throw new UserNotFoundException("No user found!");
        List<User> listAux = new ArrayList<>();
        listAux.addAll(this.userRep.listAll());
        return listAux;
    }

    public User getUserById(String id){
        this.verifyUserExists(id);
        return this.userRep.findOneByID(id);
    }

    private List<User> listUsersByName(String name, Collection<User> users){
        List<User> usersResult = new ArrayList<>();
        for(User user : users){
            if(user.getName().toLowerCase().contains(name.toLowerCase())){
                usersResult.add(user);
            }
        }
        if (usersResult.isEmpty()) throw new UserNotFoundException("No user found!");
        return usersResult;
    }

    public List<User> getUsersByName(String name){
        if (listUsersByName(name, this.userRep.listAll()).isEmpty()) throw new UserNotFoundException("No user found!");
        return listUsersByName(name, this.userRep.listAll());
    }

    public String addUser(UserDTO userDTO){
        User user = new User(userDTO.getName(), userDTO.getUsername(), userDTO.getEmail());
        this.userRep.create(user);
        return user.getId();
    }

    public String deleteUser(String id){
        this.verifyUserExists(id);
        this.userRep.delete(this.userRep.findOneByID(id));
        return id;
    }

    public User editName(String userId, String name){
        this.verifyUserExists(userId);
        User user = this.userRep.findOneByID(userId);
        user.setName(name);
        this.userRep.change(userId, user);
        return user;
    }

    public User editUserName(String userId, String userName){
        this.verifyUserExists(userId);
        User user = this.userRep.findOneByID(userId);
        user.setUsername(userName);
        this.userRep.change(userId, user);
        return user;
    }

    public User editEmail(String userId, String email){
        this.verifyUserExists(userId);
        User user = this.userRep.findOneByID(userId);
        user.setEmail(email);
        this.userRep.change(userId, user);
        return user;
    }

    public User replaceUser(String id, User user){
        this.verifyUserExists(id);
        user.setId(id);
        userRep.change(id, user);
        return user;
    }

    public boolean verifyUserExists(String id){
        if(this.userRep.findOneByID(id) == null) throw new UserNotFoundException("The user does not exist!");
        return true;
    }

}
