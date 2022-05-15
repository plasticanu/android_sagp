package com.example.comp6442_group_assignment.Search;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.Search.AVLTree.AVLTree;
import com.example.comp6442_group_assignment.Search.Tags.Tag;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchStringTokenizer;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchToken;

import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.LevenshteinDetailedDistance;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.xml.sax.SAXException;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 * This class is used for searching element.
 * TODO: complete the code and comment.
 * @Author Zhidong Piao u7139999
 */
@SuppressWarnings("ConstantConditions")
public class Search {
    private AVLTree<Post> postAVL = new AVLTree<>();
    private List<Post> allPosts = new ArrayList<>();
    private HashMap<Post, Integer> postsRank = new HashMap<>();
    // Percentage of error letters for a word allowed.
    private Integer fuzzyExtent = 0;

    public Search(Integer fuzzyExtent){
        this.fuzzyExtent = fuzzyExtent;
    }

    public Search(){
        // Default fuzzy extent.
        this.fuzzyExtent = 30;
    }

    private void insertPostToTree() throws ParserConfigurationException, IOException, SAXException {
        for (Post p : allPosts) {
            postAVL.tree = postAVL.insert(postAVL.tree, p);
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    private void rankContent(String input, ArrayList<String> exact, ArrayList<String> exclude) {
        input = input.trim();
        int onlyExcludeTag = 0;
        // FuzzyScore is used for determine how similar is the input against the content.
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);

        // Label for the post loop.
        postLoop:
        for (Post p : allPosts) {
            // Recognize the exclude tag. If detected, the post will not be put into the hashmap(postsRank)

            //if the exclude string input is not empty:
            if (!exclude.isEmpty()) {
                // case: there is only a exclude tag and no input:
                if(input.equals("") && exact.isEmpty()){
                    onlyExcludeTag = 1;
                }
                // if there is any string matched to the content, skip the following code of postLoop
                for (String ex : exclude) {
                    if (p.getContent().contains(ex)) {
                        // skip the code inside the post loop
                        // set the score to negative.
                        postsRank.put(p, postsRank.getOrDefault(p, 0) - 10000);
                        continue postLoop;
                    }
                }
            }


            // Recognize the exact matching tags.
            // If detected, and the string is found in the post, add the post with a score 200.
            // the exact string input is not empty:
            if (!exact.isEmpty()) {
                for (String e : exact) {
                    // Check if the input matches any content of the post
                    if (p.getContent().contains(e)) {
                        postsRank.put(p, postsRank.getOrDefault(p, 0) + 200 + onlyExcludeTag);
                        // skip the following code because this only searches the exactly matched posts.
                        continue postLoop;
                    }
                }
            }

            // Rank the normal text input. Based on fuzzy score.
            if (p.getContent().toLowerCase().contains(input.toLowerCase())) {
                postsRank.put(p, postsRank.getOrDefault(p, 0) + fs.fuzzyScore(p.getContent(), input) + onlyExcludeTag);
            } else {
                LevenshteinDistance ld = new LevenshteinDistance();
                SearchStringTokenizer inputT = new SearchStringTokenizer(input);
                SearchStringTokenizer contentT = new SearchStringTokenizer(p.getContent());
                float fuzzyScore = 0;

                ArrayList<String> contentTokens = new ArrayList<>();
                ArrayList<String> inputTokens = new ArrayList<>();

                while(inputT.hasNext()){
                    inputTokens.add(inputT.getCurrentToken().getString());
                    inputT.next();
                }
                while(contentT.hasNext()){

                    contentTokens.add(contentT.getCurrentToken().getString());
                    contentT.next();
                }

                int correctInputCount = 0;
                for(String i : inputTokens){
                    for(String c: contentTokens){
                        fuzzyScore = (float) ((float) ld.apply(c,i) / (float) Math.max(i.length(),c.length()));
                        if(fuzzyScore*100 <= fuzzyExtent){
                            correctInputCount ++;
                            break;
                        }
                    }
                }

                if(correctInputCount == inputTokens.size()){
                    postsRank.put(p,
                            postsRank.getOrDefault(p, 0) + fs.fuzzyScore(p.getContent(), input)  - Math.round(fuzzyScore) + onlyExcludeTag);

                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void rankUser(ArrayList<String> users) {
        // TODO: use avl tree
        for(Post p : allPosts) {
            for (String u : users) {
                if (p.getAuthor().equals(u)) {
                    postsRank.put(p, postsRank.getOrDefault(p, 0) + 100);
                } else {
                    LevenshteinDistance ld = new LevenshteinDistance();
                    float fuzzyScore = (float) ( (float) ld.apply(p.getAuthor(), u)
                            / (float) p.getAuthor().length() ) * 100;

                    if (fuzzyScore <= fuzzyExtent) {
                        postsRank.put(p,
                                postsRank.getOrDefault(p, 0) + Math.round(100 - fuzzyScore));
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void rankSharp(ArrayList<String> sharps) {
        for (Post p : allPosts) {
            SearchStringTokenizer stt = new SearchStringTokenizer(p.getContent());
            for (String s : sharps) {
                while ( stt.hasNext() ) {
                    if (stt.getCurrentToken().getType().equals(SearchToken.Type.HashTag)) {
                        if (stt.getCurrentToken().getString().equals(s)) {
                            postsRank.put(p, postsRank.getOrDefault(p, 0) + 100);
                        } else {
                            LevenshteinDistance ldd = new LevenshteinDistance();
                            float fuzzyScore = (float) ( ldd.apply(stt.getCurrentToken().getString(), s)
                                    / stt.getCurrentToken().getString().length() ) * 100;
                            if (fuzzyScore <= fuzzyExtent) {
                                postsRank.put(p,
                                        postsRank.getOrDefault(p, 0) + Math.round(100 - fuzzyScore));
                            }
                        }
                    }
                    stt.next();
                }
            }
        }
    }

    private List<Post> rankResult(HashMap<Post, Integer> input){
        // Sort the hashmap. Code from:
        // https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
        List<Map.Entry<Post,Integer>> list =
                new LinkedList<Map.Entry<Post, Integer>>(input.entrySet());

        // Sort
        Collections.sort(list, new Comparator<Map.Entry<Post, Integer>>() {
            @Override
            public int compare(Map.Entry<Post, Integer> i1, Map.Entry<Post, Integer> i2) {
                return (i2.getValue().compareTo(i1.getValue()));
            }
        });

        List<Post> result = new ArrayList<>();
        for(Map.Entry<Post, Integer> entry : list){
            if(entry.getValue() > 0){
                result.add(entry.getKey());
            }
        }
        return result;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<Post> search(String input) throws ParserConfigurationException, IOException, SAXException {
        // read all posts.
        allPosts = Post.readFromPost();

        // Insert posts to AVLTree.
        insertPostToTree();
        // Parser used to recognize whether the input has tags.

        Parser parser = new Parser(new SearchStringTokenizer(input));

        // Analyse the user input and sort them into the categories.
        String content = parser.parseWord();
        ArrayList<String> authors = new ArrayList<>();
        ArrayList<String> exclude = new ArrayList<>();
        ArrayList<String> sharps = new ArrayList<>();
        ArrayList<String> exact = new ArrayList<>();

        parser = new Parser(new SearchStringTokenizer(input));
        // update the categories.
        ArrayList<Tag> allTags = parser.parseTags();
        for(Tag t : allTags){
            switch(t.getType()){
                case "A":
                    authors.add(t.evaluate());
                    break;
                case "E":
                    exact.add(t.evaluate());
                    break;
                case "Ex":
                    exclude.add(t.evaluate());
                    break;
                case "S":
                    sharps.add(t.evaluate());
                    break;
            }
        }

        // Ranking for posts
        rankUser(authors);
        rankSharp(sharps);
        rankContent(content,exact,exclude);


        return rankResult(postsRank);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        Search s = new Search(30);
        System.out.println(s.search("king most").size());

    }





}
