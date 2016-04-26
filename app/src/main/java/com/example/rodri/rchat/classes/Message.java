package com.example.rodri.rchat.classes;

/**
 * Created by rodri on 4/25/2016.
 */
public class Message {

    private String user_from;
    private String user_to;
    private String message;

    public Message() {}

    public Message(String user_from, String user_to, String message) {
        this.user_from = user_from;
        this.user_to = user_to;
        this.message = message;
    }

    public String getUser_from() {
        return user_from;
    }

    public String getUser_to() { return user_to; }

    public String getMessage() {
        return message;
    }

}
