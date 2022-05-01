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
        //TODO: Error Message
        return false;
    }

    @Override
    public boolean logout() {
        session.changeState(new GuestState(session));
        session.user = null; // reset user
        return true;
    }

    @Override
    public boolean register(String userName, String password, String email, String firstName, String lastName, String phoneNumber) {
        //TODO: Error Message
        return false;
    }

    @Override
    public boolean deleteAccount() throws ParserConfigurationException, IOException, SAXException {
        return User.deleteAccount(session.user.getUserName());
    }

    @Override
    public void createPost(String title, String content) {
        Post post = new Post(title, content, session.profile());
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
    public List<Post> allPosts() {
        return null;
    }

    @Override
    public List<Post> search(String keyword) {
        return null;
    }
}
