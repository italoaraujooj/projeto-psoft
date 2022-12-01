package com.ufcg.psoft.scrumboard.controller;


import com.ufcg.psoft.scrumboard.dto.TaskDTO;
import com.ufcg.psoft.scrumboard.model.Task;
import com.ufcg.psoft.scrumboard.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping(value = "/task/create")
    public ResponseEntity<?> createTask(@RequestParam String userId, @RequestParam String userStoryId,
                                        @RequestParam String projectId, @RequestBody TaskDTO taskDTO) {
        String taskId;
        taskId = taskService.createTask(userId, userStoryId, projectId, taskDTO);
        return new ResponseEntity<String>("The task has been registered! ID: " + taskId, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/task/delete")
    public ResponseEntity<?> deleteTask(@RequestParam String userId, @RequestParam String userStoryId,
                                        @RequestParam String taskId, @RequestParam String projectId) {
        String myTaskId;
        myTaskId = taskService.deleteTask(userId, userStoryId, projectId, taskId);
        return new ResponseEntity<String>("The task has been removed! ID: " + myTaskId, HttpStatus.OK);
    }

    @PutMapping(value = "/task/update/title")
    public ResponseEntity<?> uptadeTaskTitle(@RequestParam String userId, @RequestParam String userStoryId,
                                             @RequestParam String projectId, @RequestParam String taskId,
                                             @RequestParam String title) {
        Task taskResponse;
        taskResponse = taskService.changeTitle(userId, userStoryId, projectId, taskId, title);
        return new ResponseEntity<String>("The task has been modified! New title: " + taskResponse.getTitle(),
                HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/task/update/description")
    public ResponseEntity<?> uptadeTaskDescription(@RequestParam String userId, @RequestParam String userStoryId,
                                                   @RequestParam String projectId, @RequestParam String taskId,
                                                   @RequestParam String description) {
        Task taskResponse;
        taskResponse = taskService.changeDescription(userId, userStoryId, projectId, taskId, description);
        return new ResponseEntity<String>("The task has been modified! New description: " + taskResponse.getTitle(),
                HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/task/search")
    public ResponseEntity<?> findTask(@RequestParam String userId, @RequestParam String userStoryId,
                                       @RequestParam String projectId,@RequestParam String taskId) {
        Task taskResponse;
        taskResponse = taskService.findTask(userId, userStoryId, projectId, taskId);
        return new ResponseEntity<Task>(taskResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/task/list")
    public ResponseEntity<?> listTasks(@RequestParam String userId, @RequestParam String userStoryId,
                                       @RequestParam String projectId) {
        List<String> tasksResponse;
        tasksResponse = taskService.listAllTasks(userId, userStoryId, projectId);
        return new ResponseEntity<List<String>>(tasksResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/task/realize/{taskId}")
    public ResponseEntity<?> realizeTask(@RequestParam String userId, @PathVariable("taskId") String taskId, @RequestParam String userStoryId, @RequestParam String projectId) {
        String taskResponse;
        taskResponse = taskService.realizeTask(userId, taskId, userStoryId, projectId);
        return new ResponseEntity<String>(taskResponse, HttpStatus.OK);
    }

}
