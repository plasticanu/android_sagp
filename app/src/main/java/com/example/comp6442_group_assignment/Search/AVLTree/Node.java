package com.example.comp6442_group_assignment.Search.AVLTree;

import androidx.annotation.NonNull;

public class Node<T>{
    public T data;
    // Used for comparing object
    public int key;
    int height;
    Node<T> left;
    Node<T> right;


    public Node(T element){
        this.data = element;
        this.key = element.hashCode();
        height = 1;
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
