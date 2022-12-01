package com.ufcg.psoft.scrumboard.controller;

import com.ufcg.psoft.scrumboard.dto.UserDTO;
import com.ufcg.psoft.scrumboard.model.User;
import com.ufcg.psoft.scrumboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value ="/users/list")
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<List<User>>(userService.listUsers(), HttpStatus.OK);
    }

    @PostMapping(value="/users/create")
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO) {
        String userId = userService.addUser(userDTO);
        return new ResponseEntity<String>("The user has been registered! ID: " + userId, HttpStatus.OK);
    }

    @DeleteMapping(value="/users/{id}/delete")
    public ResponseEntity<?> removeUser(@PathVariable("id") String id) {
        String userId;
        userId = userService.deleteUser(id);
        return new ResponseEntity<String>("The user has been removed! ID: " + userId, HttpStatus.OK);
    }

    @PutMapping(value="/user/{id}/edit/name")
    public ResponseEntity<?> editName(@PathVariable("id") String id, @RequestParam String name) {
        User user;
        user = userService.editName(id, name);
        return new ResponseEntity<String>("The user has been modified! New name: " + user.getName(),
                HttpStatus.OK);
    }

    @PutMapping(value="/user/{id}/edit/username")
    public ResponseEntity<?> editUserName(@PathVariable("id") String id, @RequestParam String userName) {
        User user;
        user = userService.editUserName(id, userName);
        return new ResponseEntity<String>("The user has been modified! New username: " + user.getUsername(),
                HttpStatus.OK);
    }

    @PutMapping(value="/user/{id}/edit/email")
    public ResponseEntity<?> editEmail(@PathVariable("id") String id, @RequestParam String email) {
        User user;
        user = userService.editEmail(id, email);
        return new ResponseEntity<String>("The user has been modified! New e-mail: " + user.getEmail(),
                HttpStatus.OK);
    }

    @GetMapping(value = "/users/search/id", params = {"id"})
    public ResponseEntity<?> getUserByID(@RequestParam String id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/users/search/name", params = {"name"})
    public ResponseEntity<?> getUserByName(@RequestParam String name) {
        List<User> users;
        users = userService.getUsersByName(name);
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);

    }
}
