package com.example.comp6442_group_assignment.Search;

import com.example.comp6442_group_assignment.MainActivity;
import com.example.comp6442_group_assignment.Search.AVLTree.AVLTree;
import com.example.comp6442_group_assignment.Search.AVLTree.Node;
import com.example.comp6442_group_assignment.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.Buffer;

public class Search {
    private Tokenizer tokenizer;
    private AVLTree<String> contentAVL;
    private AVLTree<User> userAVL;
    private int postID;


    public AVLTree<String> insertToContentTree(){
        while(this.tokenizer.hasNext()){
            contentAVL.tree = contentAVL.insertWithPostID(contentAVL.tree, tokenizer.current().toString(), postID);
            tokenizer.next();
        }
        return contentAVL;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public void setContentAVL(AVLTree<String> contentAVL) {
        this.contentAVL = contentAVL;
    }

    public void setTokenizer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

}
