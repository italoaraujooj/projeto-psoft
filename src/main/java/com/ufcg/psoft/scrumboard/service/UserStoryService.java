package com.ufcg.psoft.scrumboard.service;

import com.ufcg.psoft.scrumboard.dto.UserStoryDTO;
import com.ufcg.psoft.scrumboard.enums.Position;
import com.ufcg.psoft.scrumboard.exception.*;
import com.ufcg.psoft.scrumboard.interfaces.Event;
import com.ufcg.psoft.scrumboard.interfaces.Listener;
import com.ufcg.psoft.scrumboard.interfaces.Profile;
import com.ufcg.psoft.scrumboard.model.Project;
import com.ufcg.psoft.scrumboard.model.Task;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;
import com.ufcg.psoft.scrumboard.model.event.ProductOwnerNotify;
import com.ufcg.psoft.scrumboard.model.event.ScrumMasterNotify;
import com.ufcg.psoft.scrumboard.model.event.UserNotify;
import com.ufcg.psoft.scrumboard.model.state.Done;
import com.ufcg.psoft.scrumboard.model.state.ToVerify;
import com.ufcg.psoft.scrumboard.repository.UserStoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.ufcg.psoft.scrumboard.enums.Position.PROJECT_OWNER;
import static com.ufcg.psoft.scrumboard.enums.Position.SCRUM_MASTER;

@Service
public class UserStoryService {

    @Autowired
    private UserStoryRepository userStoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;


    public String createUserStory(String userId, String projectId, UserStoryDTO userStoryDTO) {
        checkCreationData(userId, projectId, userStoryDTO);
        UserStory userStory = new UserStory(userStoryDTO.getTitle(), userStoryDTO.getDescription());
        userStoryRepository.create(userStory);
        projectService.addUserStoryToProject(projectId, userStory);
        return userStory.getID();
    }

    public String deleteUserStory(String userId, String projectId, String userStoryId) {
        this.checkRemovalData(userId, projectId, userStoryId);
        projectService.deleteUserStoryFromProject(projectId, userStoryId);
        userStoryRepository.delete(userStoryId);
        return userStoryId;
    }

    public UserStory changeUserStoryTitle(String userId, String projectId, String userStoryId, String title) {
        this.checkModificationTitle(userId, projectId, userStoryId, title);
        UserStory userStory = this.findUserStoryById(userId, projectId, userStoryId);
        userStory.setTitle(title);
        this.userStoryRepository.change(userStoryId, userStory);
        return userStory;
    }

    public UserStory changeUserStoryDescription(String userId, String projectId, String userStoryId,
                                                String description) {
        this.checkModificationDescription(userId, projectId, userStoryId, description);
        UserStory userStory = this.findUserStoryById(userId, projectId, userStoryId);
        userStory.setDescription(description);
        this.userStoryRepository.change(userStoryId, userStory);
        return userStory;
    }

    public UserStory findUserStoryById(String userId, String projectId, String userStoryId) {
        this.checkQueryData(userId, projectId, userStoryId);
        return this.userStoryRepository.findOne(userStoryId);
    }

    public List<UserStory> findUserStoriesByTitle(String userId, String projectId, String title) {
        Collection<UserStory> userStories = this.listUserStoriesByProject(userId, projectId);

        if (title.isEmpty()) throw new EmptyTitleException("Error: Title of user story it's empty");
        List<UserStory> result = new ArrayList<>();

        for (UserStory userStory : userStories) {
            if (userStory.getTitle().toLowerCase().contains(title.toLowerCase())) {
                result.add(userStory);
            }
        }
        return result;
    }

    public Collection<UserStory> listUserStoriesByProject(String userId, String projectId){
        this.checkUserDataInProject(userId, projectId);
        return projectService.getAllUserStoriesFromProject(projectId);
    }

    public UserStory assignToUserStory(String userId, String projectId, String userStoryId) {
        Profile profile = this.projectService.getUserProfileByProject(projectId, userId);
        if((profile.getProfile().equals(SCRUM_MASTER) || (profile.getProfile().equals(PROJECT_OWNER)))) {
            throw new WithoutPermissionException("Scrum master and product owner cannot be assigned to a user story");
        }
        this.checksIfTheUserIsInTheUserStory(userId, projectId, userStoryId);

        UserStory userStory = this.userStoryRepository.findOne(userStoryId);
        User user = this.userService.getUserById(userId);
        userStory.addUser(user);
        this.userStoryRepository.change(userStoryId, userStory);
        return userStory;
    }

    public UserStory assignUserToUserStory(String scrumMasterId, String userId,
                                           String projectId, String userStoryId) {
        this.projectService.verifyScrumMaster(scrumMasterId, projectId);
        return assignToUserStory(userId, projectId, userStoryId);
    }

    public UserStory moveUserStoryToVerify(String userId, String projectId, String userStoryId) {
        verifyMoveUserStoryToVerify(userId, projectId, userStoryId);
        UserStory userStory = this.userStoryRepository.findOne(userStoryId);
        if(!userStory.getState().equals("IN_PROGRESS")){
            throw new WithoutPermissionException("Only user stories in Work in Progress can be moved To Verify");
        }
        userStory.setState(new ToVerify(userStory));
        this.userStoryRepository.change(userStoryId, userStory);
        return userStory;
    }

    private void verifyMoveUserStoryToVerify(String userId, String projectId, String userStoryId){
        checkUserDataInProject(userId, projectId);
        this.verifyUserStoryExistence(userStoryId);
        this.projectService.verifyUserStoryIsNotInProject(projectId, userStoryId);
        Profile profile = this.projectService.getUserProfileByProject(projectId, userId);

        if(!(profile.getProfile().equals(SCRUM_MASTER))) {
            checksIfTheUserIsNotInTheUserStory(userId, projectId, userStoryId);
        }
    }

    public UserStory moveUserStoryDone(String userId, String projectId, String userStoryId){
        verifyMoveUserStoryDone(userId, projectId, userStoryId);
        UserStory userStory = this.userStoryRepository.findOne(userStoryId);
        if(!userStory.getState().equals("TO_VERIFY")){
            throw new WithoutPermissionException("Only user stories in To Verify can be moved to Done");
        }
        userStory.setState(new Done(userStory));
        this.userStoryRepository.change(userStoryId, userStory);
        return userStory;    }

    private void verifyMoveUserStoryDone(String userId, String projectId, String userStoryId){
        checkUserDataInProject(userId, projectId);
        this.verifyUserStoryExistence(userStoryId);
        this.projectService.verifyUserStoryIsNotInProject(projectId, userStoryId);
        Profile profile = this.projectService.getUserProfileByProject(projectId, userId);

        if(!(profile.getProfile().equals(SCRUM_MASTER))) {
            throw new WithoutPermissionException("Only Scrum Master can move user stories in To Verify to Done");
        }
    }

    public Collection<User> getUsersFromUserStory(String userId, String projectId, String userStoryId) {
        checkUserDataInProject(userId, projectId);
        this.verifyUserStoryExistence(userStoryId);
        this.projectService.verifyUserStoryIsNotInProject(projectId, userStoryId);
        return this.userStoryRepository.findOne(userStoryId).getUsers().values();
    }

    public Collection<User> getUsersFromUserStoryFree(String projectId, String userStoryId) {
        this.verifyUserStoryExistence(userStoryId);
        this.projectService.verifyUserStoryIsNotInProject(projectId, userStoryId);
        return this.userStoryRepository.findOne(userStoryId).getUsers().values();
    }

    public void changeTasksFromUserStory(String userStoryid, Map<String, Task> tasks){
        UserStory userStory = userStoryRepository.findOne(userStoryid);
        userStory.setTasks(tasks);
        userStoryRepository.change(userStoryid, userStory);
    }

    public String addTaskToUserStory(Task task, String userStoryId){
        verifyUserStoryExistence(userStoryId);
        taskService.verifyExistence(task);
        addTask(task, userStoryId);
        return task.getID();
    }

    private void addTask(Task task, String userStoryId){
        UserStory userStory = userStoryRepository.findOne(userStoryId);
        Map<String, Task> tasks = userStory.getTasks();
        tasks.put(task.getID(), task);
        userStory.setTasks(tasks);
        userStoryRepository.change(userStoryId, userStory);
    }

    public String removeTaskFromUs(Task task, String userStoryId){
        verifyUserStoryExistence(userStoryId);
        taskService.verifyExistence(task);
        verifyTaksInUserStory(task, userStoryId);
        removeTask(task, userStoryId);
        return task.getID();
    }

    private void removeTask(Task task, String userStoryId){
        UserStory userStory = userStoryRepository.findOne(userStoryId);
        Map<String, Task> tasks = userStory.getTasks();
        tasks.remove(task.getID());
        userStory.setTasks(tasks);
        userStoryRepository.change(userStoryId, userStory);
    }

    public void changeUserStory(UserStory userStory){
        verifyUserStoryExistence(userStory.getID());
        userStoryRepository.change(userStory.getID(), userStory);
    }

    public Collection<Task> getTasksFromUserStory(String userStoryId){
        verifyUserStoryExistence(userStoryId);
        return userStoryRepository.findOne(userStoryId).getTasks().values();
    }

   public void deleteUserFromUserStory(String scrumMasterId, String userId, String userStoryId, String projectId){
        projectService.verifyProjectExists(projectId);
        projectService.verifyScrumMaster(scrumMasterId,projectId);
        verifyUserStoryExistence(userStoryId);
        userService.verifyUserExists(userId);
        checksIfTheUserIsNotInTheUserStory(userId, projectId, userStoryId);
        UserStory userStory = userStoryRepository.findOne(userStoryId);
        Map<String, User> users = userStoryRepository.findOne(userStoryId).getUsers();
        users.remove(userId);
        userStory.setUsers(users);
        userStoryRepository.change(userStoryId, userStory);
    }

    public Collection<User> getUsersFromUserStory(String idUser, String userStoryId){
        verifyUserStoryExistence(userStoryId);
        return userStoryRepository.findOne(userStoryId).getUsers().values();
    }

    private void checkCreationData(String userId, String projectId, UserStoryDTO userStoryDTO) {
        this.checkUserDataInProject(userId, projectId);
        this.verifyContents(userStoryDTO);
        this.verifyIfItsCopy(projectId, userStoryDTO);
    }

    private void checkRemovalData(String userId, String projectId, String userStoryId){
        this.checkUserDataInProject(userId, projectId);
        this.verifyUserStoryExistence(userStoryId);
        this.projectService.verifyUserStoryIsNotInProject(projectId, userStoryId);
    }

    private void checkModificationTitle(String userId, String projectId, String userStoryId,
                                        String title) {
        this.checkModificationData(userId, projectId, userStoryId);
        if(title.isEmpty()){
            throw new EmptyTitleException("Error: Title of user story it's empty");
        }
        this.verifyIsCopyTitle(title, projectId);
    }

    private void checkModificationDescription(String userId, String projectId, String userStoryId,
                                              String description) {
        this.checkModificationData(userId, projectId, userStoryId);
        if(description.isEmpty()){
            throw new EmptyTitleException("Error: Description of user story it's empty");
        }
        this.verifyIsCopyDescription(description, projectId);
    }

    private void checkModificationData(String userId, String projectId, String userStoryId) {
        this.checkUserDataInProject(userId, projectId);
        this.verifyUserStoryExistence(userStoryId);
        this.projectService.verifyUserStoryIsNotInProject(projectId, userStoryId);
    }

    private void checkQueryData(String userId, String projectId, String userStoryId) {
        checkUserDataInProject(userId, projectId);
        this.verifyUserStoryExistence(userStoryId);
        this.projectService.verifyUserStoryIsNotInProject(projectId, userStoryId);
    }

    private void checkUserDataInProject(String userId, String projectId) {
        this.userService.verifyUserExists(userId);
        this.projectService.verifyProjectExists(projectId);
        this.projectService.verifyUserIsNotInProject(projectId, userId);
    }

    private void verifyContents (UserStoryDTO userStoryDTO){

        if(userStoryDTO.getTitle().isEmpty() && userStoryDTO.getDescription().isEmpty()){
            throw new EmptyException("Error: User story doesn't have content");
        }

        if(userStoryDTO.getTitle().isEmpty()){
            throw new EmptyTitleException("Error: Title of user story it's empty");
        }

        if(userStoryDTO.getDescription().isEmpty()){
            throw new EmptyDescriptionException("Error: Description of user story it's empty");
        }

    }

    private void verifyIfItsCopy(String projectId, UserStoryDTO userStoryDTO){
        String titleToVerify = userStoryDTO.getTitle();
        String descriptionToVerify = userStoryDTO.getDescription();

        this.projectService.getAllUserStoriesFromProject(projectId).forEach(userStory ->
            {if(userStory.checkTitle(titleToVerify) && userStory.checkDescription(descriptionToVerify)){
                throw new AlreadyExistException("Error: User story already exists in project!");
            }
            else if (userStory.checkTitle(titleToVerify)){
                throw new TitleAlreadyExistException("Error: User story title already exists in project!");
            } else if (userStory.checkDescription(descriptionToVerify)) {
                throw new DescriptionAlreadyExistException("Error: User story description already exists in project!");
            }
        });
    }

    private void verifyIsCopyTitle(String title, String projectId) {
        this.projectService.getAllUserStoriesFromProject(projectId).forEach(userStory -> {
            if (userStory.checkTitle(title)){
                throw new TitleAlreadyExistException("ERROR: User story title already exists in project!");
            }
        });
    }

    private void verifyIsCopyDescription(String description, String projectId) {
        this.projectService.getAllUserStoriesFromProject(projectId).forEach(userStory -> {
            if (userStory.checkDescription(description)){
                throw new DescriptionAlreadyExistException("Error: User story description already exists in project!");
            }
        });
    }

    public void verifyUserStoryExistence(String userStoryId) {
        UserStory userStory =  userStoryRepository.findOne(userStoryId);
        if(userStory == null) throw new UserStoryNotFoundException("Error: User story does not exist.");
    }

    public void checksIfTheUserIsInTheUserStory(String userId, String projectId, String userStoryId){
        Collection<User> users = this.getUsersFromUserStory(userId, projectId, userStoryId);
        if(users.contains(this.userService.getUserById(userId))) {
            throw new AlreadyExistException("User already exists in user story.");
        }
    }

    public void checksIfTheUserIsNotInTheUserStory(String userId, String projectId, String userStoryId){
        Collection<User> users = this.getUsersFromUserStory(userId, projectId, userStoryId);
        if(!users.contains(this.userService.getUserById(userId))) {
            throw new UserNotFoundException("The user is not assigned to the user story.");
        }
    }

    public void verifyTaksInUserStory(Task task, String userStoryId){
        Map<String, Task> tasks = userStoryRepository.findOne(userStoryId).getTasks();
        if(!tasks.values().contains(task)){
            throw new TaskNotFoundException("Error: This task does not belong to this user story");
        }
    }

    public String subscribeInUserStory(String userID, String projectID, String userStoryID) {
        this.checkUserDataInProject(userID, projectID);
        this.projectService.verifyUserStoryIsNotInProject(projectID, userStoryID);

        UserStory userStory = this.userStoryRepository.findOne(userStoryID);
        if(userStory.containsListener(userID)) {
            throw new UserNotFoundException("Listener already registered!");
        }

        UserStory story = this.userStoryRepository.findOne(userStoryID);
        User user = userService.getUserById(userID);
        Profile profile = this.projectService.getUserProfileByProject(projectID, userID);

        if(profile.getProfile().equals(SCRUM_MASTER)) {
            story.registerListener(new ScrumMasterNotify(user), userID);
        } else if (profile.getProfile().equals(PROJECT_OWNER)) {
            story.registerListener(new ProductOwnerNotify(user), userID);
        } else {
            story.registerListener(new UserNotify(user), userID);
        }
        return String.format("User: %s signed up in UserStory: %s .", userID, userStoryID);
    }

    public String unscribeInUserStory(String userID, String projectID, String userStoryID) {
        this.projectService.verifyUserStoryIsNotInProject(projectID, userStoryID);
        UserStory userStory = this.userStoryRepository.findOne(userStoryID);
        if(!userStory.containsListener(userID)) {
            throw new UserNotFoundException("Listener not found!");
        }
        userStory.removeListener(userID);
        return String.format("User: %s unsigned in UserStory: %s .", userID, userStoryID);
    }

    private Listener<Event<UserStory>> selectTypeOfListener(Profile profile, User user){

        Listener<Event<UserStory>> listener;

        if(profile.getProfile().equals(SCRUM_MASTER)){
            listener = new ScrumMasterNotify(user);
        } else if (profile.getProfile().equals(PROJECT_OWNER)) {
            listener = new ProductOwnerNotify(user);
        }else{
            listener = new UserNotify(user);
        }

        return listener;
    }
}
