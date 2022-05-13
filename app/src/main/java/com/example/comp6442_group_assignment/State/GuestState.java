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
            List<User> users = User.readUsers(); // read all users
            for (User u : users) {
                if (u.getUserName().equals(userName)) {
                    session.user = u; // set user
                }
            }
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
    public Post createPost(String content) { System.out.println("Guest cannot create post."); return null; }

    @Override
    public boolean deletePost(String postId) { System.out.println("Guest cannot delete post."); return false; }

    @Override
    public boolean editPost(String postId, String content) { System.out.println("Guest cannot edit post."); return false; }

    @Override
    public boolean likePost(String postId) { System.out.println("Guest cannot like post."); return false; }

    @Override
    public boolean unlikePost(String postId) { System.out.println("Guest cannot unlike post."); return false; }

    @Override
    public boolean commentPost(String postId, String content) { System.out.println("Guest cannot comment post."); return false; }

    @Override
    public boolean followPost(String postId) throws ParserConfigurationException, IOException, SAXException { System.out.println("Guest cannot follow post."); return false; }

    @Override
    public boolean unfollowPost(String postId) throws ParserConfigurationException, IOException, SAXException { System.out.println("Guest cannot unfollow post."); return false; }

    @Override
    public User profile() {
        System.out.println("Guest cannot view profile.");
        return null;
    }

    @Override
    public boolean updateAccount(String userName, String password, String email, String firstName, String lastName, String phoneNumber) {
        System.out.println("Guest cannot update account.");
        return false;
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

    @Override
    public List<String> updateNotification() throws ParserConfigurationException, IOException, SAXException {
        System.out.println("Guest cannot update notification.");
        return null;
    }

    @Override
    public boolean clearNotification() throws ParserConfigurationException, IOException, SAXException {
        System.out.println("Guest cannot clear notification.");
        return false;
    }

    @Override
    public User requestProfile(String userName) throws ParserConfigurationException, IOException, SAXException {
        System.out.println("Guest cannot request profile.");
        return null;
    }
}
