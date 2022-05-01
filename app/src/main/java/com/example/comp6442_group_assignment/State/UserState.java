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
    public abstract void createPost(String title, String content);
    public abstract void deletePost(int postId);
    public abstract void editPost(int postId, String title, String content);
    public abstract void likePost(int postId);
    public abstract void unlikePost(int postId);
    public abstract void commentPost(int postId, String content);
    public abstract void deleteComment(int postId, int commentId);
    public abstract User profile(); // return the user profile
    public abstract List<Post> allPosts(); // return all posts
    public abstract List<Post> search(String keyword); // search the database for the keyword

}
