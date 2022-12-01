package com.ufcg.psoft.scrumboard.model;

import com.ufcg.psoft.scrumboard.enums.Status;

import java.util.UUID;

import static com.ufcg.psoft.scrumboard.enums.Status.NAO_REALIZADA;

public class Task {
    private String id;
    private String title;
    private String description;
    private Status status;
    private int hashCode;

    public Task(String titulo, String description){
        this.id = UUID.randomUUID().toString();
        this.title = titulo;
        this.description = description;
        this.status = NAO_REALIZADA;
        this.hashCode = hashCode();
    };

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public Status getStatus() {return this.status; }

    public String toStringResume() {
        return String.format("ID: %s - Title: %s - Status: %s", this.id, this.title, this.status);
    }

    @Override
    public String toString(){

        return "{\n" +
                "  \"Task ID:\": " +  this.id + ",\n" +
                "  \"Task Title\": " +  this.title + ",\n" +
                "  \"Task Description\": " + this.description + "\n" +
                "  \"Task Status\": " + this.status + "\n" +
                "}";
    }

    public String getID(){
        return this.id;
    }

    public void changeContents(String title, String description){
        this.title = title;
        this.description = description;
    }

    public boolean checkTitle(String check){
        boolean verify = this.title.equals(check);
        return verify;
    }

    public boolean checkDescription(String check){
        boolean verify = this.description.equals(check);
        return verify;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (hashCode != task.hashCode) return false;
        if (!id.equals(task.id)) return false;
        if (!title.equals(task.title)) return false;
        return description.equals(task.description);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }
}
