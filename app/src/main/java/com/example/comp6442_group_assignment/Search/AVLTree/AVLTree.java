package com.example.comp6442_group_assignment.Search.AVLTree;

import com.example.comp6442_group_assignment.Post;

import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.LevenshteinDistance;

import java.util.ArrayList;
import java.util.Locale;

/**
 * AVLTree implementation, not immutable.
 * @Author Zhidong Piao u7139999
 * @param <T>
 */
public class AVLTree<T> {
    /**
     * This field refers to the AVLTree.
     * For example: when referring to the AVLTree, the AVLTree class should not be called. Instead,
     * AVLTree.tree is the tree.
     * Code example: AVLTree<Object> avl = new AVLTree<Object>();
     *               avl.tree = avl.tree.insert(2);
     *               System.out.print(avl.tree.toString());
     * System: {value=2, leftNode=null, rightNode=null}
     */
    public Node<T> tree;

    public AVLTree() {
        tree = null;
    }

    private void updateHeight(Node<T> n){
        n.setHeight(1 + Math.max(getHeight(n.getLeft()), getHeight(n.getRight())));
    }

    private int getHeight(Node<T> n){
        if(n == null){
            return 0;
        }
        else{
            return n.getHeight();
        }
    }
    // left height - right height
    private int getBalance(Node<T> n){
        if(n == null){
            return 0;
        }
        else{
            return getHeight(n.getLeft()) - getHeight(n.getRight());
        }
    }

    /**
     * Right rotation of AVL tree. The Node is not appended a type for avoiding the null pointer exception.
     * @param n Tree root node
     * @return Right rotated tree node
     */
    private Node<T> rightRotate(Node<T> n){
        Node left = n.getLeft();
        Node lr = left.getRight();

        left.setRight(n);
        n.setLeft(lr);

        updateHeight(n);
        updateHeight(left);
        return left;
    }

    private Node<T> leftRotate(Node<T> n){
        //The node type is not specified to avoid nullPointerException.
        Node right = n.getRight();
        Node rl = right.getLeft();
        right.setLeft(n);
        n.setRight(rl);

        updateHeight(n);
        updateHeight(right);
        return right;
    }

    /**
     * AVl Tree insertion, same elements are not permitted.
     * @param n Tree root node
     * @return tree node with the element inserted.
     */
    public Node<T> insert(Node<T> n,T e){
        // Ensure the input is not null
        if(e == null)
            throw new IllegalArgumentException("AVL Insertion: Input cannot be null");

        if(n == null){
            n = new Node<T>(e);
            updateHeight(n);
            return n;
        }
        if(e.hashCode() > n.getKey()){
            n.setRight(insert(n.getRight(),e));
        }
        else if(e.hashCode() < n.getKey()){
            n.setLeft(insert(n.getLeft(),e));
        }
        if(getBalance(n) > 1){
            if(e.hashCode() < n.getLeft().getKey()){
                n = rightRotate(n);
                return n;
            }
            if(e.hashCode() > n.getLeft().getKey()){
                n.setLeft(leftRotate(n.getLeft()));
                n = rightRotate(n);

                return n;
            }
        }
        else if(getBalance(n) < -1){
            if(e.hashCode() > n.getRight().getKey()){
                n = leftRotate(n);
                return n;
            }
            if(e.hashCode() < n.getRight().getKey()){
                n.setRight(rightRotate(n.getRight()));
                n = leftRotate(n);

                return n;
            }
        }
        updateHeight(n);
        return n;

    }

    /**
     * Insertion with the post id field updated.
     * Post id field is an arraylist of a node field.
     * @param n root node
     * @param e element
     * @param postID post id
     * @return tree with element inserted and post id updated.
     */
    public Node<T> insertWithPostID(Node<T> n, T e, String postID){
        if(findNode(n, e) == null){
            n = insert(n, e);
        }
        findNode(n,e).getPostID().add(postID);

        return n;
    }

    /**
     * AVL deletion method. Delete the elements from the tree, and balance the tree.
     * @param n root node
     * @param e element to be deleted
     * @return tree with element deleted, balanced.
     */
    public Node<T> delete(Node<T> n, T e){
        // This algorithm is based on a website stated in Statement of Originality.
        if (n == null)
            return n;

        // BST deletion method
        if(e.hashCode() < n.getKey())
            n.setLeft(delete(n.getLeft(), e));
        else if(e.hashCode() > n.getKey())
            n.setRight(delete(n.getRight(), e));
        else{
            // one child is null
            if((n.getLeft() == null) || (n.getRight() == null)){
                Node<T> res = null;
                if(n.getLeft() == null){
                    res = n.getRight();
                }
                else {
                    res = n.getLeft();
                }
                // two children are null
                if(res == null){
                    res = n;
                    n = null;
                }
                else{
                    n = res;
                }

            }
            // two children
            else{
                Node<T> temp = min(n.getRight());
                n.setData(temp.getData());
                n.setKey(temp.getKey());
                n.setRight(delete(n.getRight(), temp.getData()));
            }

        }
        if( n == null){
            return n;
        }

        updateHeight(n);

        if((getBalance(n) > 1) && (getBalance(n.getLeft()) >= 0)){
            return rightRotate(n);
        }

        if((getBalance(n) > 1) && getBalance(n.getLeft()) < 0){
            n.setLeft(leftRotate(n.getLeft()));
            return rightRotate(n);
        }

        // right right case
        if((getBalance(n) < -1) && (getBalance(n.getRight()) <= 0)){
            return leftRotate(n);
        }
        if((getBalance(n) < -1) && (getBalance(n.getRight()) > 0)){
            n.setRight(rightRotate(n.getRight()));
        }

        return n;
    }

    /**
     * Find the minimum node of the input tree.
     * @param n root node of a tree
     * @return minimum node
     */
    private Node<T> min(Node<T> n) {
        Node<T> current = n;
        while(current.getLeft() != null){
            current = current.getLeft();
        }
        return current;
    }

    /**
     * Find the node, which data is equal to the element.
     * @param n root node
     * @param e element
     * @return Node, which the data equals the element.
     */
    public Node<T> findNode(Node<T> n, T e){
        if(n == null){
            return null;
        }
        else if(n.getKey() == e.hashCode()){
            return n;
        }
        else if(e.hashCode() > n.getKey()){
            return findNode(n.getRight(), e);
        }else{
            return findNode(n.getLeft(), e);
        }
    }



}
