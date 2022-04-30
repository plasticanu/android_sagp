package com.example.comp6442_group_assignment.Search.AVLTree;

import androidx.annotation.NonNull;

public class Node<T extends Comparable<T>> {
    public T data;
    int height;
    Node<T> left;
    Node<T> right;


    public Node(T element){
        this.data = element;
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
