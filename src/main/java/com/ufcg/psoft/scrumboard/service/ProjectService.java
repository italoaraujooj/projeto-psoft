package com.ufcg.psoft.scrumboard.service;

import com.ufcg.psoft.scrumboard.dto.ProjectDTO;
import com.ufcg.psoft.scrumboard.enums.Position;
import com.ufcg.psoft.scrumboard.exception.*;
import com.ufcg.psoft.scrumboard.interfaces.Profile;
import com.ufcg.psoft.scrumboard.model.Project;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;
import com.ufcg.psoft.scrumboard.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ufcg.psoft.scrumboard.enums.Position.*;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserStoryService userStoryService;

    public Map<String, Profile> getProfilesFromProject(String projectId){ return projectRepository.findOneByID(projectId).getProfiles(); }

    public Map<String, User> getAllUsersFromProject(String projectId){
        return projectRepository.findOneByID(projectId).getUsers();
    }

    public User getUserFromProject(String userId, String projectId){
        return projectRepository.findOneByID(projectId).getUsers().get(userId);
    }

    private UserStory getUS(String projectId, String userStoryId){
        return projectRepository.findOneByID(projectId).getUserStories().get(userStoryId);
    }

    public String addProject(String userID, ProjectDTO projectDTO){  // FEITO
        verifyContents(projectDTO);
        userService.verifyUserExists(userID);
        Project project = new Project(projectDTO.getName(), projectDTO.getDescription(), projectDTO.getPartnerInstitution(), userID);
        verifyIsCopy(project);
        this.projectRepository.create(project);
        project = addScrumMasterToProject(userID, project);
        this.projectRepository.change(project.getId(), project);
        return project.getId();
    }

    public String deleteProject(String scrumMasterId, String projectId){
        verifyProjectExists(projectId);
        verifyScrumMaster(scrumMasterId, projectId);
        Project project = findProjectById(projectId);
        return this.projectRepository.delete(project);
    }

    public Project editProjectName(String projectId, String scrumMasterId, String name){
        verifyScrumMaster(scrumMasterId, projectId);
        Project project = findProjectById(projectId);
        project.setName(name);
        this.projectRepository.change(projectId, project);
        return project;
    }

    public Project editProjectDescription(String projectId, String scrumMasterId, String description) {
        verifyScrumMaster(scrumMasterId, projectId);
        Project project = findProjectById(projectId);
        project.setDescription(description);
        this.projectRepository.change(projectId, project);
        return project;
    }

    public Project editProjectPartnerInstitution(String projectId, String scrumMasterId, String partnerInstitution) {
        verifyScrumMaster(scrumMasterId, projectId);
        Project project = findProjectById(projectId);
        project.setPartnerInstitution(partnerInstitution);
        this.projectRepository.change(projectId, project);
        return project;
    }

    public Project findProjectById(String projectID) {
        verifyProjectExists(projectID);
        return this.projectRepository.findOneByID(projectID);
    }

    public List<String> listAllProjects() {
        List<String> listAux = new ArrayList<>();
        projectRepository.listAll().forEach(project -> listAux.add(project.toStringResume()));
        verifyContents(listAux);
        return listAux;
    }

    public String addUserToProject(String scrumMasterId, String projectId, String userID, Position position){
        verifyProjectExists(projectId);
        verifyScrumMaster(scrumMasterId, projectId);
        verifyUserInProject(projectId, userService.getUserById(userID));
        if (position.equals(SCRUM_MASTER)){
            throw new  AlreadyExistException("You can not be a Scrum Master.");
        }
        addUser(projectId, userID, position);
        userService.replaceUser(userID, getUserFromProject(userID, projectId));
        return userID;
    }

    private void addUser(String projectId, String userId, Position position){
        if(position.equals(PROJECT_OWNER)){
            verifyProjectOwnerExists(projectId);
        }
        Map<String, Profile> profiles = getProfilesFromProject(projectId);
        Map<String, User> users = getAllUsersFromProject(projectId);
        profiles.put(userId, position.create());
        users.put(userId, userService.getUserById(userId));
        Project project = projectRepository.findOneByID(projectId);
        project.setProfiles(profiles);
        project.setUsers(users);
        if(position.equals(PROJECT_OWNER)){
            project.setProjectOwnerId(userId);
        }
        projectRepository.change(projectId, project);
    }

    public String deleteUserFromProject(String scrumMasterId, String projectID, String userID){
        if(scrumMasterId.equals(userID)){
            throw new ScrumMasterAutoDeleteException("Scrum Master can not deleted from project.");
        }
        verifyDelete(projectID, userID, scrumMasterId);
        delUser(projectID, userID);
        return userID;
    }

    private void delUser(String projectId, String userId){
        Map<String, Profile> profiles = getProfilesFromProject(projectId);
        Map<String, User> users = getAllUsersFromProject(projectId);
        profiles.remove(userId);
        users.remove(userId);
        Project project = projectRepository.findOneByID(projectId);
        project.setProfiles(profiles);
        project.setUsers(users);
        projectRepository.change(projectId, project);
    }

    public Position changeUserProfile(String projectId, String scrumMasterId, String userID, Position position){
        if(scrumMasterId.equals(userID)){
            throw new ScrumMasterEditPositionException("Scrum Master can not edit his position.");
        }
        verifyScrumMaster(scrumMasterId, projectId);
        verifyProjectExists(projectId);
        verifyUserIsNotInProject(projectId, userID);
        delUser(projectId, userID);
        addUser(projectId, userID, position);
        return position;
    }

    /*
    private Position stringToPosition(String position) {
        if (position.equalsIgnoreCase("DEVELOPER")) return DEVELOPER;
        else if (position.equalsIgnoreCase("INTERN")) return INTERN;
        else if (position.equalsIgnoreCase("PROJECT_OWNER")) return PROJECT_OWNER;
        else if (position.equalsIgnoreCase("RESEARCHER")) return RESEARCHER;
        else if (position.equalsIgnoreCase("SCRUM_MASTER")) throw new AlreadyExistException("You can not be a Scrum Master.");
        else throw new PositionNotFoundException("Position doesn't exist.");
    }
    */


    public String addUserStoryToProject(String projectId, UserStory userStory){
        verifyProjectExists(projectId);
        userStoryService.verifyUserStoryExistence(userStory.getID());
        verifyUSInProject(projectId, userStory);
        addUserStory(projectId, userStory);
        return userStory.getID();
    }

    private void addUserStory(String projectId, UserStory userStory){
        Project project = projectRepository.findOneByID(projectId);
        Map<String, UserStory> userstories = project.getUserStories();
        userstories.put(userStory.getID(), userStory);
        project.setUserStories(userstories);
        projectRepository.change(projectId, project);
    }

    public String deleteUserStoryFromProject(String projectId, String userStoryId) {
        verifyProjectExists(projectId);
        userStoryService.verifyUserStoryExistence(userStoryId);
        verifyUserStoryIsNotInProject(projectId, userStoryId);
        delUserStory(projectId, userStoryId);
        return userStoryId;
    }

    private void delUserStory(String projectId, String userStoryId){
        Project project = projectRepository.findOneByID(projectId);
        Map<String, UserStory> userstories = project.getUserStories();
        userstories.remove(userStoryId);
        project.setUserStories(userstories);
        projectRepository.change(projectId, project);
    }

    public Collection<UserStory> getAllUserStoriesFromProject(String projectId){
        verifyProjectExists(projectId);
        return projectRepository.findOneByID(projectId).getUserStories().values();
    }

    public UserStory getUSFromProject(String projectId, String userStoryId){
        verifyProjectExists(projectId);
        userStoryService.verifyUserStoryExistence(userStoryId);
        UserStory userStory = getUS(projectId, userStoryId);
        if (userStory != null) return userStory;
        else throw new UserStoryNotFoundException(String.format("Error: UserStory - %s doesn't exist in the project.", userStoryId));
    }

    public User getUserFromProject(String scrumMasterId, String userId, String projectId){
        userService.verifyUserExists(userId);
        verifyScrumMaster(scrumMasterId, projectId);
        verifyProjectExists(projectId);
        return getUserFromProject(userId, projectId);
    }

    public Collection<String> getAllUsersFromProject(String scrumMasterId, String projectId){
        userService.verifyUserExists(scrumMasterId);
        verifyScrumMaster(scrumMasterId, projectId);
        verifyProjectExists(projectId);
        List<String> users = new ArrayList<>();

        getAllUsersFromProject(projectId).values().forEach(user -> {
            users.add(user.toStringResume() + " - Position: " + this.getUserProfileByProject(projectId, user.getId()));
        });
        return users;
    }

    private Project addScrumMasterToProject(String userId, Project project) {
        Project projectNew = project;
        Map<String, User> users = new HashMap<>();
        users.put(userId, userService.getUserById(userId));
        projectNew.setUsers(users);
        return projectNew;
    }

    public Profile getUserProfileByProject(String projectId, String userId) {
        this.verifyProjectExists(projectId);
        this.userService.verifyUserExists(userId);
        this.verifyUserIsNotInProject(projectId, userId);
        return this.projectRepository.findOneByID(projectId).getProfiles().get(userId);
    }

    public List<String> checkDevelopmentStatus(String scrumMasterId, String projectId) {
        this.verifyScrumMaster(scrumMasterId, projectId);

        List<String> projectStatus = new ArrayList<>();
        this.getAllUserStoriesFromProject(projectId).forEach((userStory) -> {
            projectStatus.add(userStory.toStringResume());
        });

        return projectStatus;
    }

    private void changeContents(String projectId, String name, String description, String partnerInstitution) {
        Project project = projectRepository.findOneByID(projectId);
        project.setName(name);
        project.setDescription(description);
        project.setPartnerInstitution(partnerInstitution);
        projectRepository.change(projectId, project);
    }

    private void verifyDelete(String projectID, String userId, String scrumMasterId){
        verifyProjectExists(projectID);
        userService.verifyUserExists(userId);
        verifyScrumMaster(scrumMasterId, projectID);
        verifyUserIsNotInProject(projectID, userId);
    }
    
    private void verifyUserInProject(String projectId, User user){
        userService.verifyUserExists(user.getId());
        if(this.projectRepository.findOneByID(projectId).getUsers().containsValue(user))
            throw new AlreadyExistException(String.format("Error: User - %s already in project", user));
    }

    public void verifyProjectOwnerExists(String projectId){
        if(!projectRepository.findOneByID(projectId).getProjectOwnerId().equals("undefined")){
            throw new AlreadyExistException("Error: This project already has a Project Owner.");
        }
    }

    public void verifyUserIsNotInProject(String projectId, String userId){
        this.verifyProjectExists(projectId);
        userService.verifyUserExists(userId);
        if(!this.projectRepository.findOneByID(projectId).getUsers().containsKey(userId))
            throw new AlreadyExistException(String.format("Error: User - %s doesn't work in the project.", userId));
    }

    private void verifyUSInProject(String projectId, UserStory userStory){
        this.verifyProjectExists(projectId);
        this.userStoryService.verifyUserStoryExistence(userStory.getID());
        if(this.projectRepository.findOneByID(projectId).getUserStories().containsValue(userStory))
            throw new AlreadyExistException(String.format("Error: UserStory - %s already in project", userStory));
    }

    public void verifyUserStoryIsNotInProject(String projectId, String userStoryId){
        this.verifyProjectExists(projectId);
        this.userStoryService.verifyUserStoryExistence(userStoryId);
        if(!this.projectRepository.findOneByID(projectId).getUserStories().containsKey(userStoryId))
            throw new AlreadyExistException(String.format("Error: UserStory - %s doesn't exist in the project.", userStoryId));
    }

    private void verifyContents (ProjectDTO projectDTO){

        if(projectDTO.getName().isEmpty() && projectDTO.getDescription().isEmpty()){
            throw new EmptyException("Error: Project doesn't have content");
        }

        if(projectDTO.getName().isEmpty()){
            throw new EmptyTitleException("Error: Title of project it's empty");
        }

        if(projectDTO.getDescription().isEmpty()){
            throw new EmptyDescriptionException("Error: Description of project it's empty");
        }

    }

    private void verifyContents (List<String> contentsList){
        if(contentsList.isEmpty()){
            throw new EmptyRepositoryException("Error: Theres no project in system!");
        }
    }

    public void verifyProjectExists(String projectId){
        if(projectRepository.findOneByID(projectId) == null){
            throw new ProjectNotFoundException("Error: There's no projects in system.");
        }
    }

    private void verifyIsCopy(Project project) {
        if (this.projectRepository.listAll().contains(project)){
            throw new AlreadyExistException("Error: Project already exist.");
        }
    }

    public boolean verifyScrumMaster(String userId, String projectId){
        this.verifyUserIsNotInProject(projectId, userId);
        Project project = projectRepository.findOneByID(projectId);
        Profile profile = project.getProfiles().get(userId);
        if(!(profile.getProfile().equals(SCRUM_MASTER))) throw new UserNotBeScrumMaster("Error: This user is not a Scrum Master.");
        return true;
    }

    public boolean verifyProjectOwner(String userId, String projectId){
        this.userService.verifyUserExists(userId);
        this.verifyProjectExists(projectId);
        Project project = this.projectRepository.findOneByID(projectId);
        Profile profile = project.getProfiles().get(userId);
        if(!(profile.getProfile().equals(PROJECT_OWNER))) throw new UserNotBeProjectOwner("Error: This user is not a Project Owner");
        return true;
    }
}
