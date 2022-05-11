package com.example.comp6442_group_assignment;

import com.example.comp6442_group_assignment.State.GuestState;
import com.example.comp6442_group_assignment.State.UserState;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class UserSession {
    UserState state; // current state of the user
    String userName; // username
    boolean isLoggedIn; // whether the user is logged in
    public User user; // user profile, current user

    public UserSession() {
        userName = "Guest"; // default username
        isLoggedIn = false; // default is not logged in
        UserState defaultState = new GuestState(this); // default guest state
        changeState(defaultState);
    }

    public void changeState(UserState newState) {
        this.state = newState;
    }

    public String getUserName() {
        return userName;
    }

    public boolean login(String userName, String password) throws ParserConfigurationException, IOException, SAXException {
        boolean loggedIn = state.login(userName, password);
        if (loggedIn) {
            this.userName = userName;
            isLoggedIn = true;
        }
        return loggedIn;
    }

    public boolean logout() {
        boolean loggedOut = state.logout();
        if (loggedOut) {
            userName = "Guest";
            isLoggedIn = false;
        }
        return loggedOut;
    }

    public void createPost(String content) {
        state.createPost(content);
    }

}
