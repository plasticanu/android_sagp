package com.example.comp6442_group_assignment.Search.AVLTree;

public class AVLTree<T> {
    public Node<T> tree;

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
        Node left = n.left;
        Node lr = left.right;

        left.right = n;
        n.left = lr;

        n.height = Math.max(getHeight(n.left), getHeight(n.right) + 1);
        left.height = Math.max(getHeight(left.left), getHeight(left.right) + 1);
        return left;
    }

    private Node<T> rotateLeft(Node<T> n){
        //The node type is not specified to avoid nullPointerException.
        Node right = n.right;
        Node rl = right.left;
        right.left = n;
        n.right = rl;

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
        if(e.hashCode() > n.key){
            n.right = insert(n.right,e);
        }
        else if(e.hashCode() < n.key){
            n.left = insert(n.left,e);
        }
        if(getBalance(n) > 1){

            if(e.hashCode() < n.left.key){
                n = rotateRight(n);
                updateHeight(n);
                return n;
            }
            if(e.hashCode() > n.left.key){
                n = rotateLeft(n);
                n = rotateRight(n);
                updateHeight(n);
                return n;
            }
        }
        else if(getBalance(n) < -1){
            if(e.hashCode() > n.right.key){
                n = rotateLeft(n);
                updateHeight(n);
                return n;
            }
            if(e.hashCode() < n.right.key){
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
