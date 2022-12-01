package com.ufcg.psoft.scrumboard.model.event;

import com.ufcg.psoft.scrumboard.interfaces.Event;
import com.ufcg.psoft.scrumboard.model.UserStory;

import java.time.LocalDate;
import java.util.UUID;


public class EventUserStory implements Event<UserStory> {

    private UserStory source;
    private String id;
    private LocalDate date;

    public EventUserStory(UserStory userStory, LocalDate date){
        this.id = UUID.randomUUID().toString();
        this.source = userStory;
        this.date = date;
    }

    @Override
    public String getResume(){
        return  this.source.toString();
    }

    @Override
    public UserStory getSource() {
        return this.source;
    }
}
