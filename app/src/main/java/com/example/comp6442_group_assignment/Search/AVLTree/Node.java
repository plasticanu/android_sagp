package com.example.comp6442_group_assignment.Search.AVLTree;

import androidx.annotation.NonNull;

public class Node<T>{
    public T data;
    // Used for comparing object
    public int key;
    public int postID;
    int height;
    Node<T> left;
    Node<T> right;


    public Node(T element){
        this.data = element;
        this.key = element.hashCode();
        height = 1;
    }
    public Node(T element, int id){
        this.data = element;
        this.key = element.hashCode();
        this.postID = id;
        height = 1;
    }

    public int getHeight() {
        return height;
    }

    public int getKey() {
        return key;
    }

    public int getPostID() {
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

    public void setData(T data) {
        this.data = data;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void setLeft(Node<T> left) {
        this.left = left;
    }

    public void setPostID(int postID) {
        this.postID = postID;
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
