package com.ufcg.psoft.scrumboard.model;

import com.ufcg.psoft.scrumboard.interfaces.Profile;
import com.ufcg.psoft.scrumboard.model.profile.ScrumMaster;

import java.util.*;

public class Project {

    private String id;

    private String name;

    private String description;

    private String partnerInstitution;

    private Map<String, User> users;

    private String scrumMasterId;

    private String projectOwnerId;

    private Map<String, UserStory> userStories;

    private final int hashCode;

    private Map<String, Profile> profiles;

    public Project(String name, String description, String partnerInstitution, String scrumMasterId) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.description = description;
        this.partnerInstitution = partnerInstitution;
        this.hashCode = hashCode();
        this.scrumMasterId = scrumMasterId;
        this.projectOwnerId = "undefined";
        this.profiles = new HashMap<>();
        this.profiles.put(scrumMasterId, new ScrumMaster());
        this.users = new HashMap<>();
        this.userStories = new HashMap<>();
    }

    public String toStringResume() {
        return String.format("ID: %s - Name: %s - Partner Instituition: %s - Scrum master ID: %s - Product owner ID: %s",
                this.id, this.name, this.partnerInstitution, this.scrumMasterId, this.projectOwnerId);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id={" + id + "}\n" +
                "name={" + name + "}\n" +
                "description={" + description + "}\n" +
                "partnerInstitution={" + partnerInstitution + "}\n" +
                "scrumMasterId={" + scrumMasterId + "}\n" +
                "projectOwnerId={" + projectOwnerId + "}\n";
    }

    public Map<String, Profile> getProfiles(){ return this.profiles; }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPartnerInstitution() {
        return partnerInstitution;
    }

    public String getScrumMasterId(){
        return this.scrumMasterId;
    }

    public String getProjectOwnerId(){
        return this.projectOwnerId;
    }

    public Map<String, User> getUsers(){ return this.users; }

    public Map<String, UserStory> getUserStories() {
        return this.userStories;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPartnerInstitution(String partnerInstitution) {
        this.partnerInstitution = partnerInstitution;
    }

    public void setUsers(Map<String, User> users){
        this.users = users;
    }

    public void setProfiles(Map<String, Profile> profiles) { this.profiles = profiles; }

    public void setUserStories(Map<String, UserStory> userStories) {
        this.userStories = userStories;
    }

    public void setProjectOwnerId(String projectOwnerId){ this.projectOwnerId = projectOwnerId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return hashCode == project.hashCode && id.equals(project.id) && Objects.equals(name, project.name) && Objects.equals(description, project.description) && Objects.equals(partnerInstitution, project.partnerInstitution) && scrumMasterId.equals(project.scrumMasterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, partnerInstitution, scrumMasterId, hashCode);
    }

}
