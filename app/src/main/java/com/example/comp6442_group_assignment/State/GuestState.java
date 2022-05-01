package com.example.comp6442_group_assignment.State;

import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.User;
import com.example.comp6442_group_assignment.UserSession;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public class GuestState extends UserState {

    public GuestState(UserSession session) {
        super(session);
    }

    @Override
    public boolean login(String userName, String password) {
        User user = new User(userName, password);
        try {
            if (user.isValid()) {
                session.user = user;
                session.changeState(new LoggedInState(session));
                //TODO: Success Message
                return true;
            } else {
                user = null;
                //TODO: Error Message
                return false;
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean logout() {
        //TODO: Error Message
        return false;
    }

    @Override
    public boolean register(String userName, String password, String email, String firstName, String lastName, String phoneNumber) throws ParserConfigurationException, IOException, SAXException {
        if (User.register(userName, password, email, firstName, lastName, phoneNumber)) {
            //TODO: Success Message
            return true;
        }
        //TODO: Error Message
        return false;
    }

    @Override
    public void createPost(String title, String content) {
        //TODO: Error Message
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
        //TODO: Error Message
        return null;
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
