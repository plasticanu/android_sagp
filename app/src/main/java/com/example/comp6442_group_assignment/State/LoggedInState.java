package com.example.comp6442_group_assignment.State;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.example.comp6442_group_assignment.Comment;
import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.Search.Search;
import com.example.comp6442_group_assignment.User;
import com.example.comp6442_group_assignment.UserSession;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LoggedInState extends UserState{

    public LoggedInState(UserSession session) {
        super(session);
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return "LoggedInState";
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
            System.out.println("Delete Successful!");
            return true; // delete successful
        } else {
            System.out.println("Delete account failed.");
            return false; // delete failed
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Post createPost(String content) throws ParserConfigurationException, IOException, SAXException {
        Post post = new Post(content, session.user.getUserName());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.toString(); // get current date and time
        post.setCreateTime(dateTime);
        post.setLikes(new ArrayList<>());
        post.setComments(new ArrayList<>());
        Post.addToPost(post);
        return post;
    }

    @Override
    public boolean deletePost(String postId) throws ParserConfigurationException, IOException, SAXException {
        if (Post.belongToUser(postId, session.user.getUserName())) {
            Post.removeFromPost(postId);
            System.out.println("Delete Successful!");
            return true; // delete successful
        } else {
            System.out.println("You cannot delete other people's post.");
            return false; // delete failed
        }
    }

    @Override
    public boolean editPost(String postId, String content) throws ParserConfigurationException, IOException, SAXException {
        if (Post.belongToUser(postId, session.user.getUserName())) {
            Post.editPost(postId, content);
            System.out.println("Edit Successful!");
            return true; // edit successful
        } else {
            System.out.println("You cannot edit other people's post.");
            return false; // edit failed
        }
    }

    @Override
    public boolean likePost(String postId) throws ParserConfigurationException, IOException, SAXException {
        if (Post.belongToUser(postId, session.user.getUserName())) {
            System.out.println("You cannot like your own post.");
            return false; // like failed
        } else {
            if (Post.likePost(postId, session.user.getUserName())) {
                System.out.println("Like Successful!");
                return true; // like successful
            } else {
                System.out.println("You have already liked this post.");
                return false; // like failed
            }
        }
    }

    @Override
    public boolean unlikePost(String postId) throws ParserConfigurationException, IOException, SAXException {
        if (Post.belongToUser(postId, session.user.getUserName())) {
            System.out.println("You cannot unlike your own post.");
            return false; // unlike failed
        } else {
            if (Post.unlikePost(postId, session.user.getUserName())) {
                System.out.println("Unlike Successful!");
                return true; // unlike successful
            } else {
                System.out.println("You have not liked this post.");
                return false; // unlike failed
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean commentPost(String postId, String content) throws ParserConfigurationException, IOException, SAXException {
        Comment comment = new Comment(content, session.user.getUserName());
        comment.setTime(LocalDateTime.now().toString());
        if (Post.addToComment(postId, comment)) {
            System.out.println("Comment Successful!");
            return true; // comment successful
        } else {
            System.out.println("Post not Exist.");
            return false; // comment failed
        }
    }

    @Override
    public User profile() {
        return session.user;
    }

    @Override
    public List<Post> allPosts() throws ParserConfigurationException, IOException, SAXException {
        return Post.getAllPosts(session.user.getUserName());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Post> search(String keyword) throws ParserConfigurationException, IOException, SAXException {
        Search search = new Search();
        return search.search(keyword);
    }
}
