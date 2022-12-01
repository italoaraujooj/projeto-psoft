package com.ufcg.psoft.scrumboard.service;

import com.ufcg.psoft.scrumboard.dto.TaskDTO;
import com.ufcg.psoft.scrumboard.enums.Status;
import com.ufcg.psoft.scrumboard.exception.*;
import com.ufcg.psoft.scrumboard.model.Task;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;
import com.ufcg.psoft.scrumboard.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class TaskService {

    @Autowired
    private TaskRepository tasks;

    @Autowired
    private UserStoryService userStoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    public String createTask(String userId, String userStoryId, String projectId, TaskDTO taskDTO){
        verifyContents(taskDTO);
        verifyIsCopy(taskDTO, userStoryId);
        presetVerificationTask(userId, userStoryId, projectId);
        if(projectService.findProjectById(projectId).getScrumMasterId().equals(userId)){
            String msg = addTask(taskDTO.getTitle(),taskDTO.getDescription(), userStoryId);
            return msg;
        }else{
            userStoryService.checksIfTheUserIsNotInTheUserStory(userId, projectId, userStoryId);
            String msg = addTask(taskDTO.getTitle(),taskDTO.getDescription(), userStoryId);
            return msg;
        }
    }

    private String addTask(String title, String description, String userStoryId){
        Task task = new Task(title, description);
        tasks.create(task);
        userStoryService.addTaskToUserStory(task, userStoryId);
        return task.getID();
    }

    public Task changeTitle(String userId, String userStoryId, String projectId, String taskId, String title) {
        presetVerificationTask(userId, userStoryId, projectId);
        Task task = this.taskFinder(taskId);
        userStoryService.verifyTaksInUserStory(task, userStoryId);
        this.verifyIsCopyTitle(title, userStoryId);

        if(projectService.findProjectById(projectId).getScrumMasterId().equals(userId)){
            task.setTitle(title);
            this.tasks.change(task.getID(), task);
        }else{
            userStoryService.checksIfTheUserIsNotInTheUserStory(userId, projectId, userStoryId);
            task.setTitle(title);
            this.tasks.change(task.getID(), task);
        }
        return task;
    }

    public Task changeDescription(String userId, String userStoryId, String projectId, String taskId,
                                  String description) {
        presetVerificationTask(userId, userStoryId, projectId);
        Task task = this.taskFinder(taskId);
        userStoryService.verifyTaksInUserStory(task, userStoryId);
        this.verifyIsCopyDescription(description, userStoryId);

        if(projectService.findProjectById(projectId).getScrumMasterId().equals(userId)){
            task.setDescription(description);
            this.tasks.change(task.getID(), task);
        }else{
            userStoryService.checksIfTheUserIsNotInTheUserStory(userId, projectId, userStoryId);
            task.setDescription(description);
            this.tasks.change(task.getID(), task);
        }
        return task;
    }

    public String deleteTask(String userId, String userStoryId,String projectId,String taskId){
        presetVerificationTask(userId, userStoryId, projectId);
        if(projectService.findProjectById(projectId).getScrumMasterId().equals(userId)){
            String msg = delTask(taskId, userStoryId);
            return msg;
        }else{
            userStoryService.checksIfTheUserIsNotInTheUserStory(userId, projectId, userStoryId);
            String msg = delTask(taskId, userStoryId);
            return msg;
        }

    }

    private String delTask(String taskId, String userStoryId){
        Task task = taskFinder(taskId);
        userStoryService.removeTaskFromUs(task, userStoryId);
        tasks.delete(task);
        return taskId;
    }

    public String realizeTask(String userId, String taskId, String userStoryId, String projectId){
        userService.verifyUserExists(userId);
        projectService.verifyProjectExists(projectId);
        userStoryService.verifyUserStoryExistence(userStoryId);
        verifyPermissionTask(userId, userStoryId, projectId);
        Task task = taskFinder(taskId);
        UserStory userStory = userStoryService.findUserStoryById(userId, projectId, userStoryId);
        userStory.finishTask(taskId);
        userStoryService.changeUserStory(userStory);
        Map<String, Task> tasksMap = userStory.getTasks();
        tasksMap.replace(taskId, task);
        userStoryService.changeTasksFromUserStory(userStoryId,tasksMap);
        task.setStatus(Status.REALIZADA);
        String msg = tasks.change(taskId, task);
        return msg;
    }

    private Task taskFinder(String taskID){
        Task task = this.tasks.findOne(taskID);
        verifyExistence(task);
        return task;
    }

    public Task findTask(String userId, String userStoryId,String projectId,String taskId){
        userService.verifyUserExists(userId);
        projectService.verifyProjectExists(projectId);
        userStoryService.verifyUserStoryExistence(userStoryId);
        verifyPermissionTask(userId, userStoryId, projectId);
        Task task = taskFinder(taskId);
        return task;
    }

    public List<String>listAllTasks(String userId, String userStoryId, String projectId){
        userService.verifyUserExists(userId);
        projectService.verifyProjectExists(projectId);
        userStoryService.verifyUserStoryExistence(userStoryId);
        verifyPermissionTask(userId, userStoryId, projectId);

        List<String> listAux = new ArrayList<>();
        userStoryService.getTasksFromUserStory(userStoryId).forEach(task->
        {listAux.add(task.toStringResume());});
        return listAux;
    }

    private void verifyContents (TaskDTO taskDTO){
        if(taskDTO.getTitle().isEmpty() && taskDTO.getDescription().isEmpty()){
            throw new EmptyException("Error: Task doesn't have content");
        }
        if(taskDTO.getTitle().isEmpty()){
            throw new EmptyTitleException("Error: Title of Task it's empty");
        }
        if(taskDTO.getDescription().isEmpty()){
            throw new EmptyDescriptionException("Error: Description of Task it's empty");
        }
    }

    public void verifyExistence(Task task){

        boolean verification = this.tasks.listAll().contains(task);

        if(!verification || isEmpty(task)){
            throw new TaskNotFoundException("Error: Task not found");
        }

    }

    private void verifyIsCopy(TaskDTO taskDTO, String userStoryId){
        String titleToVerify = taskDTO.getTitle();
        String descriptionToVerify = taskDTO.getDescription();

        this.userStoryService.getTasksFromUserStory(userStoryId).forEach(task ->
            {if(task.checkTitle(titleToVerify) && task.checkDescription(descriptionToVerify)){
                throw new AlreadyExistException("Error: Task already exists in user story!");
            } else if (task.checkTitle(titleToVerify)){
                throw new TitleAlreadyExistException("Error: Task title already exists in user story!");
            } else if (task.checkDescription(descriptionToVerify)) {
                throw new DescriptionAlreadyExistException("Error: Task description already exists in user story!");
            }
        });
    }

    private void verifyIsCopyTitle(String title, String userStoryId) {
        this.userStoryService.getTasksFromUserStory(userStoryId).forEach(task -> {
            if (task.checkTitle(title)){
                throw new TitleAlreadyExistException("Error: Task title already exists in user story!");
            }
        });
    }

    private void verifyIsCopyDescription(String description, String userStoryId) {
        this.userStoryService.getTasksFromUserStory(userStoryId).forEach(task -> {
            if (task.checkDescription(description)){
                throw new DescriptionAlreadyExistException("Error: Task description already exists in user story!");
            }
        });
    }

    private void verifyPermissionTask(String userId, String userStoryId, String projectId){
        Collection<User> users = userStoryService.getUsersFromUserStoryFree(projectId, userStoryId);
        if(!(users.contains(userService.getUserById(userId)) || projectService.verifyScrumMaster(userId, projectId))){
            throw new WithoutPermissionException("Error: user without permission to realize task.");
        }
    }

    private void presetVerificationTask(String userId, String userStoryId, String projectId){
        userService.verifyUserExists(userId);
        projectService.verifyProjectExists(projectId);
        userStoryService.verifyUserStoryExistence(userStoryId);
        projectService.verifyUserStoryIsNotInProject(projectId, userStoryId);
    }

}
