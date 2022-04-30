package com.example.comp6442_group_assignment.Search.AVLTree;

public class AVLTree<T>{
    private Node<T> root;

    private void updateHeight(Node<T> n){
        n.height = 1 + Math.max(getHeight(n.left), getHeight(n.right));
    }

    private int getHeight(Node<T> n){
        if(n == null){
            return -1;
        }
        else{
            return n.height;
        }
    }
    private int getBalance(Node<T> n){
        if(n == null){
            return 0;
        }
        else{
            return getHeight(n.right) - getHeight(n.left);
        }
    }

    private Node<T> rotateRight(Node<T> n){
        Node<T> left = n.left;
        Node<T> right = n.right;
        left.right = n;
        n.left = right;
        updateHeight(n);
        updateHeight(left);
        return left;
    }

    private Node<T> rotateLeft(Node<T> n){
        Node<T> right = n.right;
        Node<T> rightLeft = right.left;
        right.left = n;
        n.right = rightLeft;
        updateHeight(n);
        updateHeight(right);
        return right;
    }

}
