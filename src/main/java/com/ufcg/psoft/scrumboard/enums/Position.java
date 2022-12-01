package com.ufcg.psoft.scrumboard.enums;

import com.ufcg.psoft.scrumboard.interfaces.Profile;
import com.ufcg.psoft.scrumboard.model.profile.*;

import java.util.Arrays;
import java.util.EnumSet;

public enum Position {

    DEVELOPER("Developer") {
        @Override
        public Profile create(){
            return new Developer();
        }
    },
    INTERN("Intern"){
        @Override
        public Profile create(){
            return new Intern();
        }
    },
    PROJECT_OWNER("Project Owner"){
        @Override
        public Profile create(){
            return new ProjectOwner();
        }
    },
    RESEARCHER("Researcher"){
        @Override
        public Profile create(){
            return new Researcher();
        }
    },
    SCRUM_MASTER("Scrum Master"){
        @Override
        public Profile create(){
            return new ScrumMaster();
        }
    };

    public abstract Profile create();
    private final String label;

    private Position(String label){
        this.label = label;
    }

    @Override
    public String toString(){
        return label;
    }

    public static Position fromLabel(String label) {
        for (Position category : values()) {
            if (category.label.equalsIgnoreCase(label)) {
                return category;
            }
        }
        throw new IllegalArgumentException(
                "Unknown enum type " + label + ", Allowed values are " + Arrays.toString(values()));
    }

    public static EnumSet<Position> getSubSetOfValues() {
        return EnumSet.of(SCRUM_MASTER, PROJECT_OWNER);
    }

}
