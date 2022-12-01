package com.ufcg.psoft.scrumboard.controller;

import com.ufcg.psoft.scrumboard.dto.UserStoryDTO;
import com.ufcg.psoft.scrumboard.model.UserStory;
import com.ufcg.psoft.scrumboard.service.UserStoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserStoryController {

    @Autowired
    private UserStoryService userStoryService;


    @PostMapping(value = "/userstory")
    public ResponseEntity<?> createUserStory(@RequestParam String userId, @RequestParam String projectId,
                                             @RequestBody UserStoryDTO userStoryDTO,
                                             UriComponentsBuilder ucBuilder) {
        String userStoryId;
        userStoryId = userStoryService.createUserStory(userId, projectId, userStoryDTO);
        return new ResponseEntity<String>("The user story has been registered! ID: " + userStoryId,
                HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/userstory")
    public ResponseEntity<?> deleteUserStory(@RequestParam String userId, @RequestParam String projectId,
                                             @RequestParam String userStoryId) {
        String myUserStoryId;
        myUserStoryId = userStoryService.deleteUserStory(userId, projectId, userStoryId);
        return new ResponseEntity<String>("The user story has been removed! ID: " + myUserStoryId, HttpStatus.OK);
    }

    @PutMapping(value = "/userstory/update/title/{userStoryId}")
    public ResponseEntity<?> updateUserStoryTitle(@RequestParam String userId, @RequestParam String projectId,
                                                  @PathVariable("userStoryId") String userStoryId,
                                                  @RequestParam String title) {

        UserStory userStoryResponse;
        userStoryResponse = userStoryService.changeUserStoryTitle(userId, projectId, userStoryId, title);
        return new ResponseEntity<String>("The user has been modified! New title: " + userStoryResponse.getTitle(),
                HttpStatus.ACCEPTED);
    }

    @PutMapping(value = "/userstory/update/description/{userStoryId}")
    public ResponseEntity<?> updateUserStoryDescription(@RequestParam String userId, @RequestParam String projectId,
                                                        @PathVariable("userStoryId") String userStoryId,
                                                        @RequestParam String description) {

        UserStory userStoryResponse;
        userStoryResponse = userStoryService.changeUserStoryDescription(userId, projectId,
                userStoryId, description);
        return new ResponseEntity<String>("The user has been modified! New description: " +
                userStoryResponse.getDescription(), HttpStatus.ACCEPTED);
    }

    @GetMapping(value = "/userstory/search/{userStoryId}")
    public ResponseEntity<?> findUserStoryById(@RequestParam String userId, @RequestParam String projectId,
                                               @PathVariable("userStoryId") String userStoryId) {

        UserStory userStoryResponse;
        userStoryResponse = userStoryService.findUserStoryById(userId, projectId, userStoryId);
        return new ResponseEntity<UserStory>(userStoryResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/userstory/search/title")
    public ResponseEntity<?> findUserStoriesByTitle(@RequestParam String userId,
                                                    @RequestParam String projectId,
                                                    @RequestParam String userStoryTitle) {
        List<UserStory> userStoriesResponse;
        userStoriesResponse = userStoryService.findUserStoriesByTitle(userId, projectId, userStoryTitle);
        return new ResponseEntity<List<UserStory>>(userStoriesResponse, HttpStatus.OK);
    }

    @GetMapping(value = "/userstories/{projectId}")
    public ResponseEntity<?> listUserStoriesFromProject(@RequestParam String userId,
                                                        @PathVariable("projectId") String projectId) {
        Collection<UserStory> userStoriesResponse;
        userStoriesResponse = userStoryService.listUserStoriesByProject(userId, projectId);
        return new ResponseEntity<Collection<UserStory>>(userStoriesResponse, HttpStatus.OK);
    }

    @PutMapping(value="userstory/assign/user")
    public ResponseEntity<?> assignToUserStory(@RequestParam String userId,
                                               @RequestParam String projectId,
                                               @RequestParam String userStoryId) {
        UserStory userStoryResponse;
        userStoryResponse = this.userStoryService.assignToUserStory(userId, projectId, userStoryId);
        return new ResponseEntity<UserStory>(userStoryResponse, HttpStatus.OK);
    }

    @PutMapping(value="userstory/scrummaster/assign/user")
    public ResponseEntity<?> assignUserToUserStory( @RequestParam String scrumMasterId,
                                                    @RequestParam String userId,
                                                    @RequestParam String projectId,
                                                    @RequestParam String userStoryId) {
        UserStory userStoryResponse;
        userStoryResponse = this.userStoryService.assignUserToUserStory(scrumMasterId, userId,
                projectId, userStoryId);
        return new ResponseEntity<UserStory>(userStoryResponse, HttpStatus.OK);
    }

    @RequestMapping(value="userstory/move/state/toverify", method = RequestMethod.PUT)
    public ResponseEntity<?> moveUserStoryToVerify(@RequestParam String userId, @RequestParam String projectId,
                                                   @RequestParam String userStoryId) {
       UserStory userStoryResponse;
       userStoryResponse = this.userStoryService.moveUserStoryToVerify(userId, projectId, userStoryId);
       return new ResponseEntity<String>(userStoryResponse.toStringResume(), HttpStatus.OK);
    }

    @RequestMapping(value="userstory/move/state/done", method = RequestMethod.PUT)
    public ResponseEntity<?> moveUserStoryDone(@RequestParam String userId, @RequestParam String projectId,
                                                   @RequestParam String userStoryId) {
        UserStory userStoryResponse;
        userStoryResponse = this.userStoryService.moveUserStoryDone(userId, projectId, userStoryId);
        return new ResponseEntity<String>(userStoryResponse.toStringResume(), HttpStatus.OK);
    }

    @RequestMapping(value="userstory/subscribe", method = RequestMethod.PUT)
    public ResponseEntity<?> subscribeInUserStory(@RequestParam String userId, @RequestParam String projectId,
                                                  @RequestParam String userStoryId) {
        String userStoryResponse;
        userStoryResponse = this.userStoryService.subscribeInUserStory(userId, projectId, userStoryId);
        return new ResponseEntity<String>(userStoryResponse, HttpStatus.OK);
    }

    @RequestMapping(value="userstory/unscribe", method = RequestMethod.PUT)
    public ResponseEntity<?> unscribeInUserStory(@RequestParam String userId, @RequestParam String projectId,
                                                  @RequestParam String userStoryId) {
        String userStoryResponse;
        userStoryResponse = this.userStoryService.unscribeInUserStory(userId, projectId, userStoryId);
        return new ResponseEntity<String>(userStoryResponse, HttpStatus.OK);
    }


}
