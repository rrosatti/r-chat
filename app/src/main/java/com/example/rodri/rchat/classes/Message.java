package com.example.rodri.rchat.classes;

/**
 * Created by rodri on 4/25/2016.
 */
public class Message {

    private String userFrom;
    private String userTo;
    private String message;

    public Message() {}

    public Message(String userFrom, String userTo, String message) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.message = message;
    }

    public String getUserFrom() {
        return userFrom;
    }

    public String getUserTo() { return userTo; }

    public String getMessage() {
        return message;
    }

}
