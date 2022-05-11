package com.example.comp6442_group_assignment.State;

import androidx.annotation.NonNull;
import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.User;
import com.example.comp6442_group_assignment.UserSession;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class GuestState extends UserState {

    public GuestState(UserSession session) {
        super(session);
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return "GuestState";
    }

    @Override
    public boolean login(String userName, String password) throws ParserConfigurationException, IOException, SAXException {
        User user = new User(userName, password);
        if (user.isValid()) {
            session.user = user;
            session.changeState(new LoggedInState(session));
            System.out.println("Login Successful! " + user.getUserName());
            return true;
        } else {
            user = null;
            System.out.println("Login Failed! Please check username and password.");
            return false;
        }
    }

    @Override
    public boolean logout() {
        System.out.println("Guest cannot logout.");
        return false;
    }

    @Override
    public boolean register(String userName, String password, String email, String firstName, String lastName, String phoneNumber) throws ParserConfigurationException, IOException, SAXException {
        if (userName.length() < 4 || password.length() < 6) {
            System.out.println("Username must be at least 4 characters long. Password must be at least 6 characters long.");
            return false;
        }
        if (User.isRegistered(userName)) {
            System.out.println("Username already exists.");
            return false;
        }
        if (User.register(userName, password, email, firstName, lastName, phoneNumber)) {
            login(userName, password);
            System.out.println("Register Successful! " + userName);
            return true;
        }
        System.out.println("Register Failed! Please check username and password.");
        return false;
    }

    @Override
    public boolean deleteAccount() {
        System.out.println("Guest cannot delete account.");
        return false;
    }

    @Override
    public void createPost(String content) {
        System.out.println("Guest cannot create post.");
    }

    @Override
    public void deletePost(String postId) {
        System.out.println("Guest cannot delete post.");
    }

    @Override
    public void editPost(String postId, String content) {
        System.out.println("Guest cannot edit post.");
    }

    @Override
    public void likePost(String postId) {
        System.out.println("Guest cannot like post.");
    }

    @Override
    public void unlikePost(String postId) {
        System.out.println("Guest cannot unlike post.");
    }

    @Override
    public void commentPost(String postId, String content) {
        System.out.println("Guest cannot comment post.");
    }

    @Override
    public void deleteComment(String postId, String commentId) {
        System.out.println("Guest cannot delete comment.");
    }

    @Override
    public User profile() {
        System.out.println("Guest cannot view profile.");
        return null;
    }

    @Override
    public List<Post> allPosts() {
        System.out.println("Guest cannot view posts.");
        return null;
    }

    @Override
    public List<Post> search(String keyword) {
        System.out.println("Guest cannot search.");
        return null;
    }
}
