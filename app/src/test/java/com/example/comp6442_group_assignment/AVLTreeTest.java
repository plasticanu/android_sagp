package com.example.comp6442_group_assignment;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.example.comp6442_group_assignment.Search.AVLTree.AVLTree;

import org.junit.Test;

public class AVLTreeTest {
    AVLTree<Integer> avl = new AVLTree<>();


    @Test
    public void InputNullTest() {
        assertEquals(2 ,(int) avl.insert(null, 2).getData());
        assertThrows(IllegalArgumentException.class, () -> {
            avl.insert(avl.tree, null);
        });
        assertNull(avl.findNode(null, 1));
    }

    @Test
    public void insertSimpleTest() {
        // test simple insertion
        avl.tree = avl.insert(avl.tree, 2);
        avl.tree = avl.insert(avl.tree, 1);
        avl.tree = avl.insert(avl.tree, 3);
        assertEquals(2, avl.tree.getHeight());
        assertEquals(2, (int) avl.tree.getData());
        assertEquals(1, (int) avl.tree.getLeft().getData());
        assertEquals(3, (int) avl.tree.getRight().getData());
        assertNull(avl.findNode(avl.tree, 4));
        assertNotNull(avl.findNode(avl.tree, 1));
    }


    @Test
    public void insertDuplicatesTest() {
        // test inserting duplicate values
        avl.tree = avl.insert(avl.tree, 3);
        avl.tree = avl.insert(avl.tree, 2);
        avl.tree = avl.insert(avl.tree, 2);
        assertEquals(2, avl.tree.getHeight());
        assertEquals(3, (int) avl.tree.getData());
        assertEquals(2, (int) avl.tree.getLeft().getData());
        assertNull(avl.tree.getRight());
        assertNull(avl.findNode(avl.tree, 1));
        assertNotNull(avl.findNode(avl.tree, 2));
    }

    @Test
    public void leftRotationTest() {
        // test left rotation
        avl.tree = avl.insert(avl.tree, 3);
        avl.tree = avl.insert(avl.tree, 2);
        avl.tree = avl.insert(avl.tree, 1);
        assertEquals(2, avl.tree.getHeight());
        assertEquals(2, (int) avl.tree.getData());
        assertEquals(1, (int) avl.tree.getLeft().getData());
        assertEquals(3, (int) avl.tree.getRight().getData());
        assertNull(avl.findNode(avl.tree, 4));
        assertNotNull(avl.findNode(avl.tree, 1));
    }

    @Test
    public void rightRotationTest() {
        // test left rotation
        avl.tree = avl.insert(avl.tree, 1);
        avl.tree = avl.insert(avl.tree, 2);
        avl.tree = avl.insert(avl.tree, 3);
        assertEquals(2, avl.tree.getHeight());
        assertEquals(2, (int) avl.tree.getData());
        assertEquals(1, (int) avl.tree.getLeft().getData());
        assertEquals(3, (int) avl.tree.getRight().getData());

        assertNull(avl.findNode(avl.tree, 4));
        assertNotNull(avl.findNode(avl.tree, 1));
    }

    @Test
    public void complexInsertTest() {
        // test inserting random values
        avl.tree = avl.insert(avl.tree, 3);
        avl.tree = avl.insert(avl.tree, 4);
        avl.tree = avl.insert(avl.tree, 8);
        avl.tree = avl.insert(avl.tree, 14);
        avl.tree = avl.insert(avl.tree, 4);
        avl.tree = avl.insert(avl.tree, 8);
        avl.tree = avl.insert(avl.tree, 2);
        avl.tree = avl.insert(avl.tree, 5);
        avl.tree = avl.insert(avl.tree, 1);
        avl.tree = avl.insert(avl.tree, 7);
        avl.tree = avl.insert(avl.tree, 10);
        assertEquals(4, avl.tree.getHeight());
        assertEquals(4, (int) avl.tree.getData());
        assertEquals(2, (int) avl.tree.getLeft().getData());
        assertEquals(8, (int) avl.tree.getRight().getData());

        assertNull(avl.findNode(avl.tree, 9));
        assertNotNull(avl.findNode(avl.tree, 14));
    }

    @Test
    public void deleteTest() {
        // test simple delete
        avl.tree = avl.insert(avl.tree, 1);
        avl.tree = avl.insert(avl.tree, 2);
        avl.tree = avl.insert(avl.tree, 3);
        avl.tree = avl.delete(avl.tree, 1);
        assertNull(avl.tree.getLeft());
        avl.tree = avl.delete(avl.tree, 3);
        assertNull(avl.tree.getRight());
        avl.tree = avl.delete(avl.tree, 2);
        assertNull(avl.tree);

        avl = new AVLTree<>();
        // test delete parent node
        avl.tree = avl.insert(avl.tree, 1);
        avl.tree = avl.insert(avl.tree, 2);
        avl.tree = avl.insert(avl.tree, 3);
        avl.tree = avl.delete(avl.tree, 2);
        assertNull(avl.findNode(avl.tree, 2));
        assertNotNull(avl.findNode(avl.tree, 1));
        assertNotNull(avl.findNode(avl.tree, 3));

        // test complex delete
        avl = new AVLTree<>();
        avl.tree = avl.insert(avl.tree, 3);
        avl.tree = avl.insert(avl.tree, 4);
        avl.tree = avl.insert(avl.tree, 8);
        avl.tree = avl.insert(avl.tree, 14);
        avl.tree = avl.insert(avl.tree, 10);
        avl.tree = avl.insert(avl.tree, 2);
        avl.tree = avl.delete(avl.tree, 3);
        assertNull(avl.findNode(avl.tree, 3));
        assertNotNull(avl.findNode(avl.tree, 14));
        avl.tree = avl.delete(avl.tree, 14);
        assertNull(avl.findNode(avl.tree, 14));
        assertNotNull(avl.findNode(avl.tree, 8));
    }

    @Test
    public void insertWithPostIDTest() {
        // test inserting with post ID
        avl.tree = avl.insert(avl.tree, 1);
        avl.tree = avl.insert(avl.tree, 2);
        avl.tree = avl.insertWithPostID(avl.tree, 1, "20");
        avl.tree = avl.insertWithPostID(avl.tree, 2, "30");
        avl.tree = avl.insertWithPostID(avl.tree, 2, "40");
        avl.tree = avl.insertWithPostID(avl.tree, 3, "25");
        assertEquals("20", avl.tree.getLeft().postID.get(0));
        assertEquals("30", avl.tree.postID.get(0));
        assertEquals("40", avl.tree.postID.get(1));
        assertEquals("25", avl.tree.getRight().postID.get(0));
    }

}
