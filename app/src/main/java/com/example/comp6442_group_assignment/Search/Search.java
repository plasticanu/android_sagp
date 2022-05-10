package com.example.comp6442_group_assignment.Search;

import android.content.Context;
import android.util.Log;

import com.example.comp6442_group_assignment.MainActivity;
import com.example.comp6442_group_assignment.R;
import com.example.comp6442_group_assignment.Search.AVLTree.AVLTree;
import com.example.comp6442_group_assignment.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class is used for searching element.
 * TODO: complete the code and comment.
 * @Author Zhidong Piao u7139999
 */
public class Search {
    private Tokenizer tokenizer;
    private AVLTree<String> contentAVL = new AVLTree<>();
    private AVLTree<User> userAVL = new AVLTree<>();
    private int postID;
    private String filePath;


    public void insertToContentTree(){
        try {
            Context ctx = MainActivity.getContext();
            // Document reader
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(ctx.getAssets().open(filePath));
            doc.getDocumentElement().normalize();

            // nList contains all elements matched to the tag name. Regardless of levels.
            NodeList nList = doc.getElementsByTagName(ctx.getString(R.string.search_read_post));

            // Loop over the elements in nList
            for(int temp = 0; temp < nList.getLength(); temp++) {
                // get the current element
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    // Get string content of the current post
                    String content = eElement.getElementsByTagName("content")
                            .item(0).getTextContent();
                    // Get post id of the current post
                    int id = Integer.parseInt(eElement.getAttribute("id"));

                    // Set a tokenizer for the content
                    tokenizer = new SentenceTokenizer(content);

                    // Loop the token, and add them to the AVL Tree.
                    while (tokenizer.hasNext()) {
                        // Insert the element to avl tree with the post id
                        contentAVL.tree = contentAVL.insertWithPostID(contentAVL.tree, tokenizer.current().toString(), id);
                        tokenizer.next();
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

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

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public AVLTree<String> getContentAVL() {
        return contentAVL;
    }
}
