package com.example.comp6442_group_assignment.State;

import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.User;
import com.example.comp6442_group_assignment.UserSession;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public abstract class UserState {
    protected UserSession session;

    public UserState(UserSession session) {
        this.session = session;
    }

    public abstract boolean login(String userName, String password) throws ParserConfigurationException, IOException, SAXException;
    public abstract boolean logout();
    public abstract boolean register(String userName, String password, String email, String firstName, String lastName, String phoneNumber) throws ParserConfigurationException, IOException, SAXException;
    public abstract boolean deleteAccount() throws ParserConfigurationException, IOException, SAXException;
    public abstract Post createPost(String content) throws ParserConfigurationException, IOException, SAXException;
    public abstract boolean deletePost(String postId) throws ParserConfigurationException, IOException, SAXException;
    public abstract boolean editPost(String postId, String content) throws ParserConfigurationException, IOException, SAXException;
    public abstract boolean likePost(String postId) throws ParserConfigurationException, IOException, SAXException;
    public abstract boolean unlikePost(String postId) throws ParserConfigurationException, IOException, SAXException;
    public abstract boolean commentPost(String postId, String content) throws ParserConfigurationException, IOException, SAXException;
    public abstract boolean followPost(String postId) throws ParserConfigurationException, IOException, SAXException;
    public abstract boolean unfollowPost(String postId) throws ParserConfigurationException, IOException, SAXException;
    public abstract User profile(); // return the user profile
    public abstract boolean updateAccount(String userName, String password, String email, String firstName, String lastName, String phoneNumber, boolean publicProfile) throws ParserConfigurationException, IOException, SAXException;
    public abstract List<Post> allPosts() throws ParserConfigurationException, IOException, SAXException; // return all posts
    public abstract List<Post> search(String keyword) throws ParserConfigurationException, IOException, SAXException; // search the database for the keyword
    public abstract List<String> updateNotification() throws ParserConfigurationException, IOException, SAXException; // update the notification
    public abstract boolean clearNotification() throws ParserConfigurationException, IOException, SAXException; // clear the notification
    public abstract User requestProfile(String userName) throws ParserConfigurationException, IOException, SAXException;
}
