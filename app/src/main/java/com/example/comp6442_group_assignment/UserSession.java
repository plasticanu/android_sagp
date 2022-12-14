package com.example.comp6442_group_assignment;

import com.example.comp6442_group_assignment.State.GuestState;
import com.example.comp6442_group_assignment.State.UserState;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

/**
 * UserSession class, should be initialized on the server each time a client connects.
 * The default state is GuestState.
 */
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

    public UserState getState() {
        return state;
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

    public boolean register(String userName, String password, String email, String firstName, String lastName, String phoneNumber) throws ParserConfigurationException, IOException, SAXException {
        if (state.register(userName, password, email, firstName, lastName, phoneNumber)) {
            login(userName, password);
            return true;
        }
        return false;
    }

    public boolean deleteAccount() throws ParserConfigurationException, IOException, SAXException {
        if (state.deleteAccount()) {
            logout();
            return true;
        }
        return false;
    }

    public Post createPost(String content) throws ParserConfigurationException, IOException, SAXException {
        if (isLoggedIn) {
            return state.createPost(content);
        }
        return null;
    }

    public boolean deletePost(String postId) throws ParserConfigurationException, IOException, SAXException {
        return state.deletePost(postId);
    }

    public boolean editPost(String postId, String content) throws ParserConfigurationException, IOException, SAXException {
        return state.editPost(postId, content);
    }

    public boolean likePost(String postId) throws ParserConfigurationException, IOException, SAXException {
        return state.likePost(postId);
    }

    public boolean unlikePost(String postId) throws ParserConfigurationException, IOException, SAXException {
        return state.unlikePost(postId);
    }

    public boolean commentPost(String postId, String content) throws ParserConfigurationException, IOException, SAXException {
        return state.commentPost(postId, content);
    }

    public boolean followPost(String postId) throws ParserConfigurationException, IOException, SAXException {
        return state.followPost(postId);
    }

    public boolean unfollowPost(String postId) throws ParserConfigurationException, IOException, SAXException {
        return state.unfollowPost(postId);
    }

    public User profile() throws ParserConfigurationException, IOException, SAXException {
        return state.profile();
    }

    public boolean updateProfile(String userName, String password, String email, String firstName, String lastName, String phoneNumber, boolean publicProfile) throws ParserConfigurationException, IOException, SAXException {
        return state.updateAccount(userName, password, email, firstName, lastName, phoneNumber, publicProfile);
    }

    public List<Post> allPosts() throws ParserConfigurationException, IOException, SAXException {
        return state.allPosts();
    }

    public List<Post> search(String keyword) throws ParserConfigurationException, IOException, SAXException {
         return state.search(keyword);
    }

    public List<String> updateNotifications() throws ParserConfigurationException, IOException, SAXException {
        return state.updateNotification();
    }

    public boolean clearNotifications() throws ParserConfigurationException, IOException, SAXException {
        return state.clearNotification();
    }

    public User requestProfile(String userName) throws ParserConfigurationException, IOException, SAXException {
        return state.requestProfile(userName);
    }

    public static Post findPostById(String postId) throws ParserConfigurationException, IOException, SAXException {
        return Post.findPostById(postId);
    }

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        UserSession userSession = new UserSession();
        List<Post> posts = userSession.search("contant");
        for (Post post: posts) {
            System.out.println(post.getPostId());
        }
    }


}
