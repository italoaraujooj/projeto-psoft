package com.ufcg.psoft.scrumboard.controller;

import com.ufcg.psoft.scrumboard.dto.ProjectDTO;
import com.ufcg.psoft.scrumboard.enums.Position;
import com.ufcg.psoft.scrumboard.enums.PositionConverter;
import com.ufcg.psoft.scrumboard.exception.EmptyException;
import com.ufcg.psoft.scrumboard.model.Project;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.service.ProjectService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping(value="projects/")
    public ResponseEntity<?> createProject(@RequestParam String userId,
                                           @RequestBody ProjectDTO projectDTO){
        String projectId = projectService.addProject(userId, projectDTO);
        return new ResponseEntity<>("The project has been registered! ID: " + projectId, HttpStatus.CREATED);
    }

    @GetMapping(value = "/projects/{projectId}")
    public ResponseEntity<?> findProject(@PathVariable("projectId") String projectId) {
        Project projectResponse;
        projectResponse = projectService.findProjectById(projectId);
        return new ResponseEntity<Project>(projectResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/projects")
    public ResponseEntity<?> listProject() {
        List<String> projectResponse;
        projectResponse = projectService.listAllProjects();
        return new ResponseEntity<List<String>>(projectResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/project/edit/name/{projectId}")
    public ResponseEntity<?> editProjectName(@PathVariable("projectId") String projectId,
                                             @RequestParam String scrumMasterId,
                                             @RequestParam String name){
        Project projectResponse = projectService.editProjectName(projectId, scrumMasterId, name);
        return new ResponseEntity<>("The project has been modified! New name: " + projectResponse.getName(),
                HttpStatus.OK);
    }

    @PutMapping(value = "/project/edit/description/{projectId}")
    public ResponseEntity<?> editProjectDescription(@PathVariable("projectId") String projectId,
                                                    @RequestParam String scrumMasterId,
                                                    @RequestParam String description){
        Project projectResponse = projectService.editProjectDescription(projectId, scrumMasterId, description);
        return new ResponseEntity<>("The project has been modified! New description: " +
                projectResponse.getDescription(), HttpStatus.OK);
    }

    @PutMapping(value = "/project/edit/partnerinstitution/{projectId}")
    public ResponseEntity<?> editProjectPartnerInstitution(@PathVariable("projectId") String projectId,
                                                           @RequestParam String scrumMasterId,
                                                           @RequestParam String partnerInstitution){
        Project projectResponse = projectService.editProjectPartnerInstitution(projectId, scrumMasterId,
                partnerInstitution);
        return new ResponseEntity<>("The project has been modified! New partner institution: " +
                projectResponse.getPartnerInstitution(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/projects/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable("projectId") String projectId,
                                           @RequestParam String scrumMasterId){
        String projectResponse = projectService.deleteProject(scrumMasterId, projectId);
        return new ResponseEntity<>("The project has been removed! ID: " + projectResponse , HttpStatus.OK);
    }

    @GetMapping(value = "/projects/profiles/")
    public ResponseEntity<?> listProfiles(){
        Position[] profilesResponse = Position.values();
        profilesResponse = new Position[]{profilesResponse[0], profilesResponse[1], profilesResponse[2], profilesResponse[3]};
        return new ResponseEntity<>(profilesResponse, HttpStatus.OK);
    }

    @PutMapping(value = "/projects/{projectId}/{userId}")
    public ResponseEntity<?> addUserToProject(@RequestParam String scrumMasterId,
                                              @PathVariable("projectId") String projectId,
                                              @PathVariable("userId") String userId,
                                              @NotNull Position position){
        String projectResponse = projectService.addUserToProject(scrumMasterId, projectId, userId, position);
        return new ResponseEntity<>( "User: " + projectResponse + " has been added as " + position, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/projects/{projectId}/{userId}")
    public ResponseEntity<?> deleteUserFromProject(@RequestParam String scrumMasterId,
                                                   @PathVariable("projectId") String projectId,
                                                   @PathVariable("userId") String userId){
        String projectResponse = projectService.deleteUserFromProject(scrumMasterId, projectId, userId);
        return new ResponseEntity<>( "User: " + projectResponse + " has been deleted from project " + projectId, HttpStatus.CREATED);
    }

    @GetMapping(value = "/projects/{projectId}/{userId}")
    public ResponseEntity<?> getUserFromProject(@RequestParam String scrumMasterId,
                                                @PathVariable("projectId") String projectId,
                                                @PathVariable("userId") String userId){
        User projectResponse = projectService.getUserFromProject(scrumMasterId, userId, projectId);
        return new ResponseEntity<>(projectResponse.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/projects/{projectId}/users")
    public ResponseEntity<?> getAllUsersFromProject(@RequestParam String scrumMasterId,
                                                    @PathVariable("projectId") String projectId){
        Collection<String> projectResponse = projectService.getAllUsersFromProject(scrumMasterId, projectId);
        return new ResponseEntity<Collection<String>>(projectResponse, HttpStatus.OK);
    }

    @PutMapping(value = "projects/{projectId}/{userId}/edit")
    public ResponseEntity<?> editUserProfile(@RequestParam String scrumMasterId, @PathVariable("projectId") String projectId,
                                             @PathVariable("userId") String userId, Position position){
        Position projectResponse = projectService.changeUserProfile(projectId, scrumMasterId, userId, position);
        return new ResponseEntity<>("User's Profile updated: " + userId + " - " + projectResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/project/development/status")
    public ResponseEntity<?> checkDevelopmentStatus(@RequestParam String scrumMasterId,
                                                    @RequestParam String projectId) {
        List<String> projectResponse = projectService.checkDevelopmentStatus(scrumMasterId, projectId);
        return new ResponseEntity<List<String>>(projectResponse, HttpStatus.OK);
    }

    @InitBinder
    public void initBinder(final WebDataBinder webdataBinder) {
        webdataBinder.registerCustomEditor(Position.class, new PositionConverter());
    }

}
