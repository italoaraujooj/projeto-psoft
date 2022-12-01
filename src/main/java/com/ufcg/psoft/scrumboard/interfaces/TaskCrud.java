package com.ufcg.psoft.scrumboard.interfaces;

import com.ufcg.psoft.scrumboard.model.Task;


public interface TaskCrud extends Crud<String, Task> {
    public Task findOne(String id);

}
