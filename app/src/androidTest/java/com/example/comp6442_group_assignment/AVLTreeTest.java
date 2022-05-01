package com.example.comp6442_group_assignment;

import static org.junit.Assert.assertEquals;

import android.util.Log;

import com.example.comp6442_group_assignment.Search.AVLTree.AVLTree;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AVLTreeTest {
    private AVLTree<Object> avl;

    @Before
    public void createAVLTree(){
        avl = new AVLTree<>();
    }

    @After
    public void cleanAVLTree(){
        avl = null;
    }

    @Test
    public void simpleInsertionTest() {
        avl.tree = avl.insert(avl.tree,"2");
        assertEquals("{value=2, leftNode=null, rightNode=null}", avl.tree.toString());
        Log.i("simpleInsertionTest", "One element inserted to an empty tree, the tree is\n" + avl.tree.toString() +
                "\nExpected: {value=2, leftNode=null, rightNode=null}");
    }

    @Test
    public void insertToRightTest1(){
        avl.tree = avl.insert(avl.tree, 2);
        avl.tree = avl.insert(avl.tree, 10);
        Log.i("insertToRightTest1", "Actual: \n" +
                avl.tree.toString() +
                "\nExpected: {value=2, leftNode=null, rightNode={value=10, leftNode=null, rightNode=null}}");
        assertEquals("{value=2, leftNode=null, rightNode={value=10, leftNode=null, rightNode=null}}", avl.tree.toString());

    }
    @Test
    public void insertToRightTest2(){
        avl.tree = avl.insert(avl.tree, 10);
        avl.tree = avl.insert(avl.tree, 20);
        avl.tree = avl.insert(avl.tree, 21);
        Log.i("insertToRightTest2", "Actual: \n" +
                avl.tree.toString() +
                "\nExpected: {value=20, " +
                "leftNode={value=10, leftNode=null, rightNode=null}, " +
                "rightNode={value=21, leftNode=null, rightNode=null}}");
        assertEquals("{value=20, " +
                "leftNode={value=10, leftNode=null, rightNode=null}, " +
                "rightNode={value=21, leftNode=null, rightNode=null}}",avl.tree.toString());
    }

    @Test
    public void rightRotateSimpleTest(){
        avl.tree = avl.insert(avl.tree, 100);
        avl.tree = avl.insert(avl.tree, 50);
        avl.tree = avl.insert(avl.tree, 20);

    }
}
