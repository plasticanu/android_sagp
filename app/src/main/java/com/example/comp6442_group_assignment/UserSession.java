package com.example.comp6442_group_assignment;

import com.example.comp6442_group_assignment.State.GuestState;
import com.example.comp6442_group_assignment.State.UserState;

public class UserSession {
    UserState state; // current state of the user
    String userName; // username
    boolean isLoggedIn; // whether the user is logged in

    public UserSession() {
        userName = "Guest"; // default username
        isLoggedIn = false; // default is not logged in
        UserState defaultState = new GuestState(this); // default guest state
        changeState(defaultState);
    }

    public void changeState(UserState newState) {
        this.state = newState;
    }

    public boolean login(String userName, String password) {
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


}
