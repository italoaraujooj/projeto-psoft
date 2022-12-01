package com.ufcg.psoft.scrumboard.model.profile;

import com.ufcg.psoft.scrumboard.enums.Position;
import com.ufcg.psoft.scrumboard.interfaces.Profile;

import static com.ufcg.psoft.scrumboard.enums.Position.INTERN;

public class Intern implements Profile {

    private final Position profile;
    public Intern(){
        this.profile = INTERN;
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
