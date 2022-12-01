package com.ufcg.psoft.scrumboard.repository;

import com.ufcg.psoft.scrumboard.interfaces.ProjectCrud;
import com.ufcg.psoft.scrumboard.model.Project;
import org.springframework.stereotype.Repository;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ProjectRepository implements ProjectCrud {

    private Map<String, Project> projects;

    public ProjectRepository() {
        this.projects = new HashMap<>();
    }

    @Override
    public Project findOneByID(String id) {
        return projects.get(id);
    }

    @Override
    public String create(Project project) {
        this.projects.put(project.getId(), project);
        return String.format("Projeto: %s, criado.", project.getId());
    }

    @Override
    public String change(String id, Project project) {
        this.projects.replace(id, project);
        return String.format("Projeto: %s, modificado.", id);
    }

    @Override
    public String delete(Project project) {
        String id = project.getId();
        this.projects.remove(id);
        return String.format("Projeto: %s, removido.", id);
    }

    public Collection<Project> listAll() {
        return this.projects.values();
    }
}

