package com.ufcg.psoft.scrumboard.model.profile;

import com.ufcg.psoft.scrumboard.enums.Position;
import com.ufcg.psoft.scrumboard.interfaces.Profile;

import static com.ufcg.psoft.scrumboard.enums.Position.PROJECT_OWNER;

public class ProjectOwner implements Profile {

    private final Position profile;
    public ProjectOwner(){
        this.profile = PROJECT_OWNER;
    }

    @Override
    public Position getProfile() {
        return this.profile;
    }

    @Override
    public String toString(){
        return this.profile.toString();
    }

}
