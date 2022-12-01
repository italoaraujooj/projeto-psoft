package com.ufcg.psoft.scrumboard.service;

import com.ufcg.psoft.scrumboard.enums.Position;
import com.ufcg.psoft.scrumboard.enums.PositionSet;
import com.ufcg.psoft.scrumboard.exception.AlreadyExistException;
import com.ufcg.psoft.scrumboard.exception.UserNotBeProjectOwner;
import com.ufcg.psoft.scrumboard.exception.UserNotBeScrumMaster;
import com.ufcg.psoft.scrumboard.exception.WithoutPermissionException;
import com.ufcg.psoft.scrumboard.interfaces.Profile;
import com.ufcg.psoft.scrumboard.model.Project;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.model.UserStory;
import com.ufcg.psoft.scrumboard.model.report.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportService {

    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserStoryService userStoryService;

    public String creatReportByProject(String userID, String projectID, PositionSet option) {

        projectService.verifyUserIsNotInProject(projectID, userID);

        User user  = userService.getUserById(userID);
        Project project = projectService.findProjectById(projectID);

        Set<String> usersIdOfProject = new HashSet<>(projectService.getAllUsersFromProject(projectID).keySet());

        Collection<UserStory> userStoriesList = new ArrayList<>(projectService.getAllUserStoriesFromProject(projectID));

        Map<String, List<UserStory>> userStoriesByUser = mapUserToUserStories(userStoriesList, projectID);

        if(!project.getProjectOwnerId().isEmpty()){
            usersIdOfProject.remove(project.getProjectOwnerId());
        }

        if (!project.getScrumMasterId().isEmpty()){
            usersIdOfProject.remove(project.getScrumMasterId());
        }

        String msg = "";

        if(option == PositionSet.SCRUM_MASTER){
            if(projectService.verifyScrumMaster(userID, projectID)){
                Report report = new ScrumMasterReport.Builder(userStoriesByUser, userStoriesList, usersIdOfProject ).addDescriptors(project, user).build();
                msg = report.getReport();
            }
        } else if(option == PositionSet.PROJECT_OWNER){
            if(projectService.verifyProjectOwner(userID,projectID)){
                Report report = new ProjectOwnerReport.Builder(userStoriesList).addDescriptors(project, user).build();
                msg = report.getReport();
            }
        } else{
            Profile profile = projectService.getUserProfileByProject(projectID, userID);
            if (profile.getProfile().equals(Position.SCRUM_MASTER)||profile.getProfile().equals(Position.PROJECT_OWNER)){
                throw new WithoutPermissionException("Error: The profile of user it's: Scrum Master or Project Owner of the selected project, please select de correct option.");
            }else{
                Report report = new UserReport.Builder(profile, userStoriesByUser.get(userID), userStoriesList).addDescriptors(project, user).build();
                msg = report.getReport();
            }
        }

        return msg;
    }

    private Map<String, List<UserStory>> mapUserToUserStories (Collection<UserStory> userStoriesList, String projectID){

        Map<String, List<UserStory>> userStoriesByUser = new HashMap<>();

        Set<String> users = projectService.getAllUsersFromProject(projectID).keySet();

        for (String user: users) {
            for (UserStory userStory: userStoriesList) {
                try{
                    userStoryService.checksIfTheUserIsInTheUserStory(user, projectID, userStory.getID());
                } catch(AlreadyExistException e){
                    userStoriesByUser.computeIfAbsent(user, k-> new ArrayList<>()).add(userStory);
                }
            }
         }

        return userStoriesByUser;

    }




}
