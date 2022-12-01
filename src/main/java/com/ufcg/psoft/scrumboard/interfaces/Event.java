package com.ufcg.psoft.scrumboard.interfaces;

import com.ufcg.psoft.scrumboard.model.UserStory;

public interface Event<T> {

    public T getSource();
    public String getResume();

}
