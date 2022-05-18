package com.example.comp6442_group_assignment;

import android.os.Build;
import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to create a new post from partial information.
 * Since time must be auto generated, it is not included in the constructor.
 * The author is set as one observer, and other list variables are set as empty.
 */
public class PostFactory {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Post createNewPost(String username, String content) {
        Post post = new Post(content, username);
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.toString(); // get current date and time
        post.setCreateTime(dateTime);
        post.setLikes(new ArrayList<>()); // set likes to empty list
        post.setComments(new ArrayList<>()); // set comments to empty list
        List<String> observers = new ArrayList<>();
        observers.add(username); // add author to observers
        post.setObservers(observers);
        return post;
    }
}
