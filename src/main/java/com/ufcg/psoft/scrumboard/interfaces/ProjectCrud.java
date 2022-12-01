package com.ufcg.psoft.scrumboard.interfaces;

import com.ufcg.psoft.scrumboard.model.Project;

public interface ProjectCrud extends Crud<String, Project> {
    public Project findOneByID(String id);
}
