package com.example.comp6442_group_assignment.Search;

import android.content.Context;
import android.util.Log;

import com.example.comp6442_group_assignment.MainActivity;
import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.R;
import com.example.comp6442_group_assignment.Search.AVLTree.AVLTree;
import com.example.comp6442_group_assignment.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private ArrayList<Integer> contentRank = new ArrayList<>();
    private HashMap<Integer,Integer> rank = new HashMap<>();
    private ArrayList<Post> posts = new ArrayList<>();

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
                    posts.add(new Post(content,id));
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

    public void rankContent(String input){
        contentRank.clear();
        Tokenizer tokenizer = new SentenceTokenizer(input);
        ArrayList<Integer> postIDs = new ArrayList<>();
        HashMap<Integer,Integer> idWithScore = new HashMap<>();
        while(tokenizer.hasNext()){
            com.example.comp6442_group_assignment.Search.AVLTree.Node<String> n = contentAVL.findNode(contentAVL.tree,tokenizer.current().toString());
            postIDs.addAll(n.getPostID());
            tokenizer.next();
        }
        // Make hashmap from postIds.
        // The key is the post id, the value is its frequency.
        for(int i : postIDs){
            if(idWithScore.containsKey(i)){
                int score = idWithScore.get(i);
                idWithScore.put(i,  score + 1);
            }
            else{
                idWithScore.put(i, 1);
            }
        }

        // Sort the hashmap. Code from:
        // https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
        List<Map.Entry<Integer,Integer>> list =
                new LinkedList<Map.Entry<Integer, Integer>>(idWithScore.entrySet());

        // Sorting
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> i1, Map.Entry<Integer, Integer> i2) {
                return (i2.getValue().compareTo(i1.getValue()));
            }
        });

        HashMap<Integer,Integer> result =
                new LinkedHashMap<Integer,Integer>();

        for(Map.Entry<Integer,Integer> aa : list){
            result.put(aa.getKey(),aa.getValue());
        }

        contentRank.addAll(result.keySet());
        rank = result;
    }

    public static void main(String[] args) {
        HashMap<Integer,Integer> test = new HashMap<>();
        test.put(123,500);
        test.put(8755, 10);
        test.put(500, 11);
        test.put(23, 1);

        // Sort the hashmap. Code from:
        // https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
        List<Map.Entry<Integer,Integer>> list =
                new LinkedList<Map.Entry<Integer, Integer>>(test.entrySet());

        // Sorting
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> i1, Map.Entry<Integer, Integer> i2) {
                return (i2.getValue().compareTo(i1.getValue()));
            }
        });

        HashMap<Integer,Integer> result =
                new LinkedHashMap<Integer,Integer>();

        for(Map.Entry<Integer,Integer> aa : list){
            result.put(aa.getKey(),aa.getValue());
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

    public ArrayList<Integer> getContentRank() {
        return contentRank;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
