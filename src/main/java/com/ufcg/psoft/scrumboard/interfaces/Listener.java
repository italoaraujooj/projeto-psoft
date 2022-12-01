package com.ufcg.psoft.scrumboard.interfaces;

import com.ufcg.psoft.scrumboard.model.event.EventUserStory;

public interface Listener<Event> {
    
    public void changedDescription(Event event);

    public void changedState(Event event);

    public void finishedTask(Event event);

}
