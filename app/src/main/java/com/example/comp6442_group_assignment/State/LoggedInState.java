package com.example.comp6442_group_assignment.State;

import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.User;
import com.example.comp6442_group_assignment.UserSession;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class LoggedInState extends UserState{

    public LoggedInState(UserSession session) {
        super(session);
    }

    @Override
    public boolean login(String userName, String password) {
        System.out.println("You are already logged in.");
        return false;
    }

    @Override
    public boolean logout() {
        session.changeState(new GuestState(session));
        session.user = null; // reset user
        System.out.println("Logout Successful!");
        return true;
    }

    @Override
    public boolean register(String userName, String password, String email, String firstName, String lastName, String phoneNumber) {
        System.out.println("You are already logged in.");
        return false;
    }

    @Override
    public boolean deleteAccount() throws ParserConfigurationException, IOException, SAXException {
        boolean result = User.deleteAccount(session.user.getUserName());
        if (result) {
            logout();
            System.out.println("Delete Successful!");
            return true; // delete successful
        } else {
            System.out.println("Delete account failed.");
            return false; // delete failed
        }
    }

    @Override
    public void createPost(String content) {
        Post post = new Post(content, session.user.getUserName());
        //TODO: To implement
    }

    @Override
    public void deletePost(int postId) {

    }

    @Override
    public void editPost(int postId, String title, String content) {

    }

    @Override
    public void likePost(int postId) {

    }

    @Override
    public void unlikePost(int postId) {

    }

    @Override
    public void commentPost(int postId, String content) {

    }

    @Override
    public void deleteComment(int postId, int commentId) {

    }

    @Override
    public User profile() {
        return session.user;
    }

    @Override
    public List<Post> allPosts() { return null; }

    @Override
    public List<Post> search(String keyword) {
        return null;
    }
}
