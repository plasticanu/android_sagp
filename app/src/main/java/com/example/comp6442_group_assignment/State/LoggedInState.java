package com.example.comp6442_group_assignment.State;

import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.User;
import com.example.comp6442_group_assignment.UserSession;

import java.util.List;

public class LoggedInState extends UserState{

    public LoggedInState(UserSession session) {
        super(session);
    }

    @Override
    public boolean login(String userName, String password) {
        return false;
    }

    @Override
    public boolean logout() {
        return false;
    }

    @Override
    public void createPost(String title, String content) {

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
