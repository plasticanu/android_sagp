package com.example.comp6442_group_assignment.Search.AVLTree;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Node for AVLTree
 * TODO:  Comment, getters and setters.
 * @param <T>
 */
public class Node<T>{
    private T data;
    // Used for comparing object
    private final int key;
    public ArrayList<String> postID = new ArrayList<>();
    int height;
    private Node<T> left;
    private Node<T> right;


    public Node(T element){
        this.data = element;
        this.key = element.hashCode();
        height = 1;
    }
    public Node(T element, String id){
        this.data = element;
        this.key = element.hashCode();
        this.postID.add(id);
        height = 1;
    }

    public int getHeight() {
        return height;
    }

    public int getKey() {
        return key;
    }

    public ArrayList<String> getPostID() {
        return postID;
    }

    public Node<T> getLeft() {
        return left;
    }

    public Node<T> getRight() {
        return right;
    }

    public T getData() {
        return data;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }
    public void setRight(Node<T> right) {
        this.right = right;
    }

    public void setPostID(ArrayList<String> postID) {
        this.postID = postID;
    }

    public void setData(T data) {
        this.data = data;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "value=" + data +
                ", leftNode=" + left +
                ", rightNode=" + right +
                '}';
    }

}
