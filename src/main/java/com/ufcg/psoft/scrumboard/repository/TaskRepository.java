package com.ufcg.psoft.scrumboard.repository;

import com.ufcg.psoft.scrumboard.interfaces.TaskCrud;
import com.ufcg.psoft.scrumboard.model.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TaskRepository implements TaskCrud {

   private Map<String, Task> tasks;

   public TaskRepository(){
      this.tasks = new HashMap<String,Task>();
   }

   @Override
   public String create(Task object) {
      this.tasks.put(object.getID(), object);
      return String.format("Task: %s, criada.", object.getID());
   }

   @Override
   public String change(String id, Task object) {
      this.tasks.replace(id, object);
      return String.format("Task: %s, modificada.", id);
   }

   @Override
   public String delete(Task object) {
      String id = object.getID();;
      this.tasks.remove(id);
      return String.format("Task: %s, removida.", id);
   }

   @Override
   public Collection<Task> listAll() {
      return this.tasks.values();
   }

   @Override
   public Task findOne(String id) {
      return this.tasks.get(id);
   }
}
