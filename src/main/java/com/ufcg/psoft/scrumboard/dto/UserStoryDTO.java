package com.ufcg.psoft.scrumboard.dto;

public class UserStoryDTO {

    private String title;

    private String description;

    private String projectID;

    public UserStoryDTO(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

}
