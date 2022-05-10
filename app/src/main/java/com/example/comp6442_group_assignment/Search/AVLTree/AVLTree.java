package com.example.comp6442_group_assignment.Search.AVLTree;

/**
 * AVLTree implementation, not immutable.
 * @Author Zhidong Piao u7139999
 * TODO: Deletion method and comment. AND code style.
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

    private Node<T> rightRotate(Node<T> n){
        Node left = n.left;
        Node lr = left.right;

        left.right = n;
        n.left = lr;

        //TODO: change the code format the same as rotateLeft.
        n.height = Math.max(getHeight(n.left), getHeight(n.right) + 1);
        left.height = Math.max(getHeight(left.left), getHeight(left.right) + 1);
        return left;
    }

    private Node<T> leftRotate(Node<T> n){
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
                n = rightRotate(n);
                return n;
            }
            if(e.hashCode() > n.left.key){
                n.left = leftRotate(n.left);
                n = rightRotate(n);

                return n;
            }
        }
        else if(getBalance(n) < -1){
            if(e.hashCode() > n.right.key){
                n = leftRotate(n);
                return n;
            }
            if(e.hashCode() < n.right.key){
                n.right = rightRotate(n.right);
                n = leftRotate(n);

                return n;
            }
        }
        updateHeight(n);
        return n;

    }
    public Node<T> insertWithPostID(Node<T> n, T e, int postID){
        if(findNode(n, e) == null){
            n = insert(n, e);
        }
        findNode(n,e).getPostID().add(postID);

        return n;
    }
    public Node<T> delete(Node<T> n, T e){

        if (n == null)
            return n;

        // BST deletion method
        if(e.hashCode() < n.key)
            n.left = delete(n.left, e);
        else if(e.hashCode() > n.key)
            n.right = delete(n.right, e);
        // Case: needs rotation
        // TODO: balance
        return n;
    }

    public Node<T> findNode(Node<T> n, T e){
        if(n == null){
            return null;
        }
        else if(n.key == e.hashCode()){
            return n;
        }
        else if(e.hashCode() > n.key){
            return findNode(n.getRight(), e);
        }else{
            return findNode(n.getLeft(), e);
        }
    }

}
