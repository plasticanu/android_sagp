package com.example.comp6442_group_assignment;

import static org.junit.Assert.assertEquals;

import com.example.comp6442_group_assignment.Search.AVLTree.AVLTree;
import com.example.comp6442_group_assignment.Search.AVLTree.Node;

import org.junit.Test;

public class AVLTreeTest {
    @Test
    public void insertionTest() {
        AVLTree<String> tree = new AVLTree<>();
        tree.tree = tree.insert(tree.tree,"2");
        tree.tree = tree.insert(tree.tree,"23");
        tree.tree = tree.insert(tree.tree,"24");
        tree.tree = tree.insert(tree.tree,"10");
        tree.tree = tree.insert(tree.tree,"11");
        tree.tree = tree.insert(tree.tree,"13");
        tree.tree = tree.insert(tree.tree,"15");
        System.out.println(tree);
    }
}
