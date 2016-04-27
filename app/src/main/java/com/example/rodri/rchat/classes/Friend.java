package com.example.rodri.rchat.classes;

/**
 * Created by rodri on 4/27/2016.
 */
public class Friend {

    private String userName;
    private String friendName;

    public Friend() { }

    public Friend(String userName, String friendName) {
        this.userName = userName;
        this.friendName = friendName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFriendName() {
        return friendName;
    }

}
