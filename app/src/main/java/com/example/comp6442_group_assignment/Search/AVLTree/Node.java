package com.example.comp6442_group_assignment.Search.AVLTree;

import androidx.annotation.NonNull;

import java.util.ArrayList;

/**
 * Node for AVLTree
 * TODO:  Comment, getters and setters.
 * @param <T>
 */
public class Node<T>{
    public T data;
    // Used for comparing object
    public int key;
    public ArrayList<String> postID = new ArrayList<>();
    int height;
    Node<T> left;
    Node<T> right;


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
