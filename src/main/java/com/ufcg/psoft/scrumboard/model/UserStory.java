package com.ufcg.psoft.scrumboard.model;

import java.time.LocalDate;
import java.util.*;

import com.ufcg.psoft.scrumboard.enums.Status;
import com.ufcg.psoft.scrumboard.interfaces.Event;
import com.ufcg.psoft.scrumboard.interfaces.Listener;
import com.ufcg.psoft.scrumboard.interfaces.Subject;
import com.ufcg.psoft.scrumboard.interfaces.UserStoryState;
import com.ufcg.psoft.scrumboard.model.event.EventUserStory;
import com.ufcg.psoft.scrumboard.model.state.ToDo;


public class UserStory implements Subject {

    private String id;

    private String title;

    private String description;

    private UserStoryState state;

    private Map<String, User> users;

    private Map<String, Listener<Event<UserStory>>> listeners;

    private Map<String, Task> tasks;

    private String idLastTaskFinished;

    private enum EventTypeEnum{
        DESCRIPTION, STATE, FINISHED;
    }

    private static final String DESCRIPTION = EventTypeEnum.DESCRIPTION.name();
    private static final String STATE = EventTypeEnum.STATE.name();
    private static final String FINISHED = EventTypeEnum.FINISHED.name();

    public UserStory(String title, String description) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.tasks = new HashMap<>();
        this.state = new ToDo(this);
        this.users = new HashMap<>();
        this.listeners = new HashMap<>();
    }

    public String getID() {
        return this.id;
    }

    public void setState(UserStoryState state) {
        this.state = state;
        notifyListeners(STATE);
    }

    public String getState() {
        return this.state.toString();
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyListeners(DESCRIPTION);
    }

    public String getDescription() {
        return this.description;
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }

    public Map<String, Task> getTasks() {
        return this.tasks;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public void addUser(User user) {
        this.state.addResponsible(user);
    }

    public void finishTask(String idTask) {
        if(this.checkIfTaskIsNotFinished(idTask)) {
            this.state.finishTask(idTask);
            this.idLastTaskFinished = idTask;
            notifyListeners(FINISHED);
        }
    }

    public String getIdLastTaskFinished() {
        return this.idLastTaskFinished;
    }

    private boolean checkIfTaskIsNotFinished(String idTask) {
        if(this.tasks.containsKey(idTask) &&
                this.tasks.get(idTask).getStatus() == Status.NAO_REALIZADA) return true;
        return false;
    }

    public Map<String, User> getUsers() {
        return this.users;
    }

    @Override
    public synchronized void registerListener(Listener listener, String id) {
        this.listeners.put(id, listener);
    }

    @Override
    public synchronized void removeListener(String id) {
        this.listeners.remove(id);
    }

    public boolean containsListener(String id) {
        return this.listeners.containsKey(id);
    }

    @Override
    public void notifyListeners(String evenType) {
        Event<UserStory> event = new EventUserStory(this, LocalDate.now());
        this.fireEvent(event, evenType);
    }

    private void fireEvent(Event<UserStory> event, String evenType){
        Collection<Listener<Event<UserStory>>> listenersCopy;

        synchronized(this){
            listenersCopy = copyListeners();
        }
        for (Listener<Event<UserStory>> listener : listenersCopy) {
            eventSelection(event, listener, evenType);
        }
    }

    private Collection<Listener<Event<UserStory>>> copyListeners() {
        Collection<Listener<Event<UserStory>>> copy = new ArrayList<>();
        copy.addAll(this.listeners.values());
        return copy;
    }

    private void eventSelection (Event<UserStory> event, Listener<Event<UserStory>> listener, String evenType){

        if(evenType.equals(DESCRIPTION)){
            listener.changedDescription(event);
        }
        if(evenType.equals(FINISHED)){
            listener.finishedTask(event);
        }
        if(evenType.equals(STATE)) {
            listener.changedState(event);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStory userStory = (UserStory) o;
        return Objects.equals(id, userStory.id) && Objects.equals(title, userStory.title) &&
                Objects.equals(description, userStory.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description);
    }


    public boolean checkTitle(String check) {
        boolean verify = this.title.equals(check);
        return verify;
    }

    public boolean checkDescription(String check) {
        boolean verify = this.description.equals(check);
        return verify;
    }

    public String toStringResume() {
        return String.format("User story ID: %s - Title: %s - Status: %s", this.id, this.title, this.state.toString());
    }

    public String toString() {
        return String.format("id = %s%n" +
                            "title = %s%n" +
                            "description = %s%n" +
                            "state = %s",
                            this.id, this.title, this.description,
                            this.state.toString());
    }

}