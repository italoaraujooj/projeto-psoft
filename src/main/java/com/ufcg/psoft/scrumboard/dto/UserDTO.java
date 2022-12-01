package com.ufcg.psoft.scrumboard.dto;

import com.ufcg.psoft.scrumboard.interfaces.Profile;

public class UserDTO {

    private String name;

    private String email;

    private String username;

    public UserDTO(String name, String email, String username) {
        this.name = name;
        this.email = email;
        this.username = username;
    }

    public String getName() { return name;    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
