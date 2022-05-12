package com.example.comp6442_group_assignment.Search;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.comp6442_group_assignment.MainActivity;
import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.R;
import com.example.comp6442_group_assignment.Search.AVLTree.AVLTree;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchStringTokenizer;
import com.example.comp6442_group_assignment.Search.Tokenizer.Tokenizer;
import com.example.comp6442_group_assignment.User;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
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
    private AVLTree<String> contentAVL = new AVLTree<>();
    private List<Post> allPosts = new ArrayList<>();
    private List<User> allUsers = new ArrayList<>();
    private void insertToContentTree() throws ParserConfigurationException, IOException, SAXException {
        allPosts = Post.readFromPost();
        for(Post p : allPosts){
            SearchStringTokenizer stt = new SearchStringTokenizer(p.getContent());
            while(stt.hasNext()){
                contentAVL.tree = contentAVL.insertWithPostID(contentAVL.tree, stt.getCurrentToken().getString(), p.getPostId());
                stt.next();
            }

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private HashMap<Post, Integer> rankContent(String input){

        SearchStringTokenizer tokenizer = new SearchStringTokenizer(input);
        ArrayList<String> postIDs = new ArrayList<>();
        HashMap<String,Integer> idWithScore = new HashMap<>();
        while(tokenizer.hasNext()){
            com.example.comp6442_group_assignment.Search.AVLTree.Node<String> n = contentAVL.findNode(contentAVL.tree,tokenizer.getCurrentToken().getString());
            postIDs.addAll(n.getPostID());
            tokenizer.next();
        }
        // Make hashmap from postIds.
        // The key is the post id, the value is its frequency.
        for(String i : postIDs){
            if(idWithScore.containsKey(i)){
                int score = idWithScore.get(i);
                idWithScore.put(i,  score + 1);
           }
           else{
                idWithScore.put(i, 1);
            }
        }

        List<Map.Entry<String,Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(idWithScore.entrySet());


        HashMap<Post,Integer> result =
                new LinkedHashMap<Post,Integer>();

        for(Map.Entry<String,Integer> e : list){
            Post resultPost = allPosts.stream().filter(x -> x.getPostId().equals(e.getKey())).findAny().orElse(null);
            result.put(resultPost, e.getValue());
        }
        return result;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private HashMap<Post, Integer> rankUser(ArrayList<User> input){

        HashMap<Post, Integer> result = new HashMap<>();
        for(User u : input){
            Post r = allPosts.stream().filter(x -> x.getAuthor().equals(u.getUserName())).findAny().orElse(null);
            result.put(r,100);
        }
        return result;
    }

    private List<Post> rankResult(HashMap<Post, Integer> input){
        // Sort the hashmap. Code from:
        // https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
        List<Map.Entry<Post,Integer>> list =
                new LinkedList<Map.Entry<Post, Integer>>(input.entrySet());

        // Sorting
        Collections.sort(list, new Comparator<Map.Entry<Post, Integer>>() {
            @Override
            public int compare(Map.Entry<Post, Integer> i1, Map.Entry<Post, Integer> i2) {
                return (i2.getValue().compareTo(i1.getValue()));
            }
        });

        HashMap<Post,Integer> resultHashMap =
                new LinkedHashMap<Post,Integer>();

        for(Map.Entry<Post,Integer> e : list){
            resultHashMap.put(e.getKey(), e.getValue());
        }
        List<Post> result = new ArrayList<>(resultHashMap.keySet());
        result.addAll(resultHashMap.keySet());
        return result;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Post> search(String input) throws ParserConfigurationException, IOException, SAXException {
        insertToContentTree();
        Parser parser = new Parser(new SearchStringTokenizer(input));
        String content = parser.parseWord();
        HashMap<Post,Integer> searchResult = new HashMap<>();
        if(content != null){
            HashMap<Post, Integer> contentResult = rankContent(content);
            searchResult.putAll(contentResult);
        }
        ArrayList<String> authorTags = parser.parseAuthorTag();
        if( !authorTags.isEmpty()){
            allUsers = User.readUsers();
            List<User> authors = allUsers;
            ArrayList<User> users = new ArrayList<>();
            for(String s : authorTags){
                for(User u : allUsers){
                    if(u.getUserName().equals(s)){
                        users.add(u);
                    }
                }
            }
            HashMap<Post,Integer> userResult = rankUser(users);
            if(searchResult.isEmpty()){
                searchResult.putAll(userResult);
            }
            else{
                for(Map.Entry<Post, Integer> e : userResult.entrySet()){
                    searchResult.merge(e.getKey(),e.getValue(), Integer::sum);
                }
            }
        }
        return rankResult(searchResult);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        Search s = new Search();
        System.out.println(s.search("where are you?"));
    }




}
