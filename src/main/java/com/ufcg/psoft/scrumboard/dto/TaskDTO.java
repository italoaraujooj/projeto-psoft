package com.ufcg.psoft.scrumboard.dto;

public class TaskDTO {

    private String title;
    private String description;

    public TaskDTO(String title, String description){
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
