package com.example.rodri.rchat.classes;

/**
 * Created by rodri on 4/26/2016.
 */
public class User {

    private String name;
    private String email;

    public User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

}
