package com.ufcg.psoft.scrumboard.dto;

public class ProjectDTO {

    private String name;

    private String description;

    private String partnerInstitution;

    public ProjectDTO(String name, String description, String partnerInstitution) {
        this.name = name;
        this.description = description;
        this.partnerInstitution = partnerInstitution;
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
}
