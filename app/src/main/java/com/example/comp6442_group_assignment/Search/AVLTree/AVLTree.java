package com.example.comp6442_group_assignment.Search.AVLTree;

public class AVLTree<T extends Comparable<T>> {
    public Node<T> tree;
    public AVLTree(Node<T> d){
        this.tree = d;
    }

    public AVLTree() {
        tree = null;
    }

    private void updateHeight(Node<T> n){
        n.height = 1 + Math.max(getHeight(n.left), getHeight(n.right));
    }

    private int getHeight(Node<T> n){
        if(n == null){
            return 0;
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
            return getHeight(n.left) - getHeight(n.right);
        }
    }

    private Node<T> rotateRight(Node<T> n){
        Node<T> left = n.left;
        Node<T> lr = left.right;

        left.right = n;
        n.left = lr;

        n.height = Math.max(getHeight(n.left), getHeight(n.right) + 1);
        left.height = Math.max(getHeight(left.left), getHeight(left.right) + 1);
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

    public Node<T> insert(Node<T> n,T e){
        // Ensure the input is not null
        if(e == null)
            throw new IllegalArgumentException("AVL Insertion: Input cannot be null");

        if(n == null){
            n = new Node<T>(e);
            updateHeight(n);
            return n;
        }
        if(e.compareTo(n.data) > 0){
            n.right = insert(n.right,e);
        }
        else if(e.compareTo(n.data) <0){
            n.left = insert(n.left,e);
        }
        if(getBalance(n) > 1){

            if(e.compareTo(n.left.data) < 0){
                n = rotateRight(n);
                updateHeight(n);
                return n;
            }
            if(e.compareTo(n.left.data) > 0){
                n = rotateLeft(n);
                n = rotateRight(n);
                updateHeight(n);
                return n;
            }
        }
        else if(getBalance(n) < -1){
            if(e.compareTo(n.right.data) > 0){
                n = rotateLeft(n);
                updateHeight(n);
                return n;
            }
            if(e.compareTo(n.right.data) < 0){
                n = rotateRight(n.right);
                n = rotateLeft(n);
                updateHeight(n);
                return n;
            }
        }
        updateHeight(n);
        return n;

    }

}
