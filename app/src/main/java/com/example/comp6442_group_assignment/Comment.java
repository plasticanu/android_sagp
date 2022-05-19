package com.example.comp6442_group_assignment;

import org.jetbrains.annotations.NotNull;

/**
 * Comment class. This class is used to store comments. Comments are objects that will be stored as variables in the
 * Post class.
 */
public class Comment {
    private String content;
    private String author;
    private String time;

    public Comment(String content, String author, String time) {
        this.content = content;
        this.author = author;
        this.time = time;
    }

    public Comment(String content, String author) {
        this.content = content;
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @NotNull
    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +
                "| author='" + author + '\'' +
                "| time='" + time + '\'' +
                '}';
    }
}
