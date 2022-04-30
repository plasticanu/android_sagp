package com.example.comp6442_group_assignment;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import com.example.comp6442_group_assignment.Search.AVLTree.AVLTree;

import org.junit.Test;

public class AVLTreeTest {
    @Test
    public void simpleInsertionTest() {
        AVLTree<String> tree = new AVLTree<>();
        tree.tree = tree.insert(tree.tree,"2");
        assertEquals("{value=2, leftNode=null, rightNode=null}", tree.tree.toString());
        Log.i("simpleInsertionTest", "One element inserted to an empty tree, the tree is\n" + tree.tree.toString() +
                "\nExpected: {value=2, leftNode=null, rightNode=null}");
    }

    @Test
    public void insertToRightTest1(){
        AVLTree<Object> tree = new AVLTree<>();
        tree.tree = tree.insert(tree.tree, 2);
        tree.tree = tree.insert(tree.tree, 10);
        Log.i("insertToRightTest1", "Actual: \n" +
                tree.tree.toString() +
                "\nExpected: {value=2, leftNode=null, rightNode={value=10, leftNode=null, rightNode=null}}");
        assertEquals("{value=2, leftNode=null, rightNode={value=10, leftNode=null, rightNode=null}}", tree.tree.toString());

    }
    @Test
    public void insertToRightTest2(){
        AVLTree<Object> tree = new AVLTree<>();
        tree.tree = tree.insert(tree.tree, 10);
        tree.tree = tree.insert(tree.tree, 20);
        tree.tree = tree.insert(tree.tree, 30);
        tree.tree = tree.insert(tree.tree, 40);
        tree.tree = tree.insert(tree.tree, 50);
        tree.tree = tree.insert(tree.tree, 25);
        Log.i("insertToRightTest2", "One element inserted to an empty tree, the tree is\n" +
                tree.tree.toString());

    }
}
