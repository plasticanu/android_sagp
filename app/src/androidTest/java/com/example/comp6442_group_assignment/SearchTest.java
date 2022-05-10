package com.example.comp6442_group_assignment;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.comp6442_group_assignment.Search.AVLTree.AVLTree;
import com.example.comp6442_group_assignment.Search.SentenceTokenizer;
import com.example.comp6442_group_assignment.Search.Token;
import com.example.comp6442_group_assignment.Search.Tokenizer;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class SearchTest {
    @Test
    public void documentReaderTest() {
        try {
            // Application context used for getting assets.
            Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
            // Document reader
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(ctx.getAssets().open("posts.xml"));
            doc.getDocumentElement().normalize();

            // Start getting data
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            // nList contains all elements matched to the tag name. Regardless of levels.
            NodeList nList = doc.getElementsByTagName("post");

            for(int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("author : "
                            + eElement.getElementsByTagName("author")
                            .item(0).getTextContent());
                    System.out.println("post id: "
                            + eElement.getAttribute("id"));
                    System.out.println("likes_count : "
                            + eElement.getElementsByTagName("likes_count")
                            .item(0).getTextContent());
                    System.out.println("temp: " + temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void insertContentToTreeTest(){
        Tokenizer to;
        try {
            // Application context used for getting assets.
            Context ctx = InstrumentationRegistry.getInstrumentation().getTargetContext();
            // Document reader
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(ctx.getAssets().open("posts.xml"));
            doc.getDocumentElement().normalize();

            // Start getting data
            Log.i("Root element : ",doc.getDocumentElement().getNodeName());

            // nList contains all elements matched to the tag name. Regardless of levels.
            NodeList nList = doc.getElementsByTagName("post");
            AVLTree<String> avl = new AVLTree<>();
            for(int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String content = eElement.getElementsByTagName("content")
                            .item(0).getTextContent();
                    int id = Integer.parseInt(eElement.getAttribute("id"));
                    to = new SentenceTokenizer(content);

                    while(to.hasNext()){
                        avl.tree = avl.insertWithPostID(avl.tree, to.current().toString(), id);
                        to.next();
                    }

                    Log.i("insert content : " , content);
                    Log.i("post id: " ,String.valueOf(id));
                }
            }
            System.out.println(avl.tree.getHeight());
            ArrayList<Integer> a = avl.findNode(avl.tree, "This").postID;
            System.out.println(a);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
