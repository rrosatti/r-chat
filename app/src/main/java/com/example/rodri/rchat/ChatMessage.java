package com.example.rodri.rchat;

/**
 * Created by rodri on 4/25/2016.
 */
public class ChatMessage {

    private String name;
    private String message;

    public ChatMessage() {}

    public ChatMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

}
