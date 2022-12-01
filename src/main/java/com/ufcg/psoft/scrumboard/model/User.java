package com.ufcg.psoft.scrumboard.model;

import com.ufcg.psoft.scrumboard.interfaces.Event;
import com.ufcg.psoft.scrumboard.interfaces.Listener;

import java.util.*;

public class User implements Listener<Event<UserStory>> {

    protected String id;

    protected String name;

    protected String username;

    protected String email;

    //protected Profile profile;

    public User(String name, String username, String email){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.username = username;
        this.email = email;
        //this.profile = new Default();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }


//    public void setProfile(Profile profile) {
//        this.profile = profile;
//    }
//
//    public String getProfile() {
//        return profile.getProfile();
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String toStringResume() {
        return String.format("ID: %s - Name: %s - User name: %s - Email: %s", this.id, this.name, this.username,
                this.email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                //", profile=" + profile +
                '}';
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Map<String, List<Event>> events = new HashMap<>();

    @Override
    public void changedDescription(Event<UserStory> event) {
        this.events.computeIfAbsent(event.getSource().getID(), k-> new ArrayList<>()).add(event);
    }


    @Override
    public void changedState(Event<UserStory> event) {
        this.events.computeIfAbsent(event.getSource().getID(), k-> new ArrayList<>()).add(event);
    }

    @Override
    public void finishedTask(Event<UserStory> event) {
        this.events.computeIfAbsent(event.getSource().getID(), k-> new ArrayList<>()).add(event);
    }
}
