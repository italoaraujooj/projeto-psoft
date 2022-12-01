package com.ufcg.psoft.scrumboard.model.profile;

import com.ufcg.psoft.scrumboard.enums.Position;
import com.ufcg.psoft.scrumboard.interfaces.Profile;

import static com.ufcg.psoft.scrumboard.enums.Position.SCRUM_MASTER;

public class ScrumMaster implements Profile {

    private final Position profile;

    public ScrumMaster() {
        this.profile = SCRUM_MASTER;
    }

    @Override
    public Position getProfile() {
        return profile;
    }

    @Override
    public String toString(){
        return this.profile.toString();
    }

}


