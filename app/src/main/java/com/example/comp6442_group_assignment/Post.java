package com.example.comp6442_group_assignment;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String title;
    private String content;
    private User author;
    private int likes;
    private String createTime;
    private ArrayList<Comment> comments;

    public Post(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
    public Post(String content, int likes){
        this.content = content;
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public int getLikes() {
        return likes;
    }
}
