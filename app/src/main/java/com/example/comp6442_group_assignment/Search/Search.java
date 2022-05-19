package com.example.comp6442_group_assignment.Search;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.Search.AVLTree.AVLTree;
import com.example.comp6442_group_assignment.Search.AVLTree.Node;
import com.example.comp6442_group_assignment.Search.Tags.Tag;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchStringTokenizer;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchToken;

import org.apache.commons.text.similarity.FuzzyScore;
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
 * This class implements search function.
 * The number of error letters allowed per word is set to be 30% as default.
 * The search result is ranked based on scores.
 * @Author Zhidong Piao u7139999
 */
@SuppressWarnings("ConstantConditions")
public class Search {
    // Singleton instance. Only one instance is allowed.
    private static Search instance = null;

    // AVLTree holding the tokens parsed by SearchStringTokenizer.
    // The tokens are from post contents.
    private static AVLTree<String> contentAVL = new AVLTree<>();

    // AVLTree holding all the posts.
    private static AVLTree<Post> postAVL = new AVLTree<>();

    private static List<Post> allPosts = new ArrayList<>();

    // Hashmap that used for ranking the search results.
    // The key is Post, the value is its score that how similar it fits to the user input.
    private static HashMap<Post, Integer> postsRank = new HashMap<>();

    // Percentage of error letters for a word allowed.
    private Integer fuzzyExtent;

    /**
     * Constructor for Search. If no integer passed, the default fuzzy extent is set to 30%.
     */
    private Search() throws ParserConfigurationException, IOException, SAXException {
        // Default fuzzy extent.
        this.fuzzyExtent = 30;
        allPosts = Post.readFromPost();
    }

    /**
     * Constructor for Search. Takes an integer input as fuzzy extent.
     * @param fuzzyExtent the percentage of error letters allowed per word.
     */
    public Search(Integer fuzzyExtent) throws ParserConfigurationException, IOException, SAXException {
        this.fuzzyExtent = fuzzyExtent;
        allPosts = Post.readFromPost();
    }

    public static Search getInstance() throws ParserConfigurationException, IOException, SAXException {
        if (instance == null) {
            instance = new Search();
        }
        return instance;
    }
    public Search getInstance(Integer fuzzyExtent) throws ParserConfigurationException, IOException, SAXException {
        if (instance == null) {
            instance = new Search(fuzzyExtent);
        } if (!fuzzyExtent.equals(instance.fuzzyExtent)) {
            instance.fuzzyExtent = fuzzyExtent;
        }
        return instance;
    }

    /**
     * This method insert all Posts to postAVL and contentAVL.
     * The postAVL contains posts.
     * The contentAVL contains all tokens that the SearchStringTokenizer parsed from the post contents,
     * and the corresponding post ids are updated.
     */
    private void insertPostToTree() {
        for (Post p : allPosts) {
            postAVL.tree = postAVL.insert(postAVL.tree, p);
            SearchStringTokenizer stt = new SearchStringTokenizer(p.getContent());
            while(stt.hasNext()){
                contentAVL.tree = contentAVL.insertWithPostID(contentAVL.tree,stt.getCurrentToken().getString(), p.getPostId());
                stt.next();
            }
        }

    }

    /**
     * This method rank the search result based on user input when it contains plain text and Exact or Exclude tags.
     * The postRank is updated.
     * @param input user plain text input, not tags.
     * @param exact ArrayList of exact tag expressions.
     * @param exclude ArrayList of exclude tag expressions.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void rankContent(String input, ArrayList<String> exact, ArrayList<String> exclude) {
        // delete white spaces of the user input.
        input = input.trim();

        // variable that recognize whether the search query contains only exclude tags.
        // if set to be 1, the posts that do not contain the exclude tag expressions will have a minimum score of 1.
        int onlyExcludeTag = 0;

        // FuzzyScore is used for determine how similar is the input against the content.
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);

        // Label for the post loop.
        postLoop:
        for (Post p : allPosts) {
            // Recognize the exclude tag. If detected, the post will not be put into the hashmap(postsRank)

            //if the exclude string input is not empty:
            if (!exclude.isEmpty()) {
                // case: there are only exclude tags and no input:
                if (input.equals("") && exact.isEmpty()) {
                    onlyExcludeTag = 1;
                }
                // if there is any string matched to the content, skip the following code of postLoop
                // and set the score to -10000, for which the map entry will be deleted when rankResult is called.
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
        }

        // Rank the normal text input. Based on fuzzy score.
        rankContentSimpleInput(input);
        fuzzySearch(input,onlyExcludeTag);
    }

    /**
     * This method rank the search result based on user input when it only contains words.
     * The whole search process is rank the posts that contain the user input, then call fuzzy search.
     * @param input user plain text input.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void rankContent(String input){
        // Rank the normal text input. Based on fuzzy score.
        rankContentSimpleInput(input);
        fuzzySearch(input,0);
    }

    /**
     * This method gives the hashmap entries with scores. Rank the search result based on user input.
     * Fuzzy search is not called but the search score is based on fuzzy score.
     * For whole plain text search process, see rankContent.
     * If there are several words as input and at least one of the words matched an element on
     * content avl tree, the result entry will be put to hashmap as one of the search result.
     * @param input user plain text input.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void rankContentSimpleInput(String input){
        // The tokenizer is used to token the user input. Therefore, see if any words matched the post content.
        SearchStringTokenizer stt = new SearchStringTokenizer(input);
        while(stt.hasNext()){
            // Find node from the content avl tree. If a node is found, the user input at least matched a post.
            Node<String> foundNode = contentAVL.findNode(contentAVL.tree, stt.getCurrentToken().getString());

            // Case when the user input word does not match a post.
            if(foundNode != null){
                ArrayList<String> postIDs = foundNode.getPostID();
                for(String id :postIDs){
                    for(Post p : allPosts){
                        if(p.getPostId().equals(id)){
                            FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
                            // put an entry with a fuzzy score as value.
                            postsRank.put(p, postsRank.getOrDefault(p, 0) + fs.fuzzyScore(p.getContent(), input));
                        }
                    }
                }

            }
            stt.next();
        }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void rankUser(ArrayList<String> users) {
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

    /**
     * Rank the posts based on sharp tags.
     * @param sharps ArrayList of sharp tags. (hash tags)
     */
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
        contentAVL = new AVLTree<>();
        postAVL = new AVLTree<>();
        // Insert posts to AVLTree.
        insertPostToTree();

        // clear the previous result
        postsRank.clear();

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
        if(authors.isEmpty() && exclude.isEmpty() && sharps.isEmpty() && exact.isEmpty()){
            rankContent(input);
        }
        else{
            rankContent(content,exact,exclude);
        }

        return rankResult(postsRank);
    }


//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
//        Search s = getInstance();
////        System.out.println(s.search("contant").size());
//
//        for (Post post: s.search("apparition")) {
//            System.out.println(post.getPostId());
//        }
//
//    }

    public void delete(Post p){
        Node<Post> foundPost = postAVL.findNode(postAVL.tree, p);
        if(foundPost != null){
            SearchStringTokenizer stt = new SearchStringTokenizer(p.getContent());
            while(stt.hasNext()){
                Node<String> foundToken = contentAVL.findNode(contentAVL.tree,stt.getCurrentToken().getString());
                foundToken.getPostID().remove(p.getPostId());
                if(foundToken.getPostID().isEmpty()){
                    contentAVL.delete(contentAVL.tree, foundToken.getData());
                }

                stt.next();
            }
        }
    }

    public void insert(Post p){
        allPosts.add(p);
        SearchStringTokenizer stt = new SearchStringTokenizer(p.getContent());
        while(stt.hasNext()){
            contentAVL.tree = contentAVL.insertWithPostID(contentAVL.tree, stt.getCurrentToken().getString(), p.getPostId());
            stt.next();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fuzzySearch(String input, Integer onlyExcludeTag){
        for(Post p : allPosts) {
            LevenshteinDistance ld = new LevenshteinDistance();
            SearchStringTokenizer inputT = new SearchStringTokenizer(input);
            SearchStringTokenizer contentT = new SearchStringTokenizer(p.getContent());
            float fuzzyScore = 0;

            ArrayList<String> contentTokens = new ArrayList<>();
            ArrayList<String> inputTokens = new ArrayList<>();

            while ( inputT.hasNext() ) {
                inputTokens.add(inputT.getCurrentToken().getString());
                inputT.next();
            }
            while ( contentT.hasNext() ) {

                contentTokens.add(contentT.getCurrentToken().getString());
                contentT.next();
            }

            int correctInputCount = 0;
            for (String i : inputTokens) {
                for (String c : contentTokens) {
                    fuzzyScore = (float) ( (float) ld.apply(c, i) / (float) Math.max(i.length(), c.length()) );
                    if (fuzzyScore * 100 <= fuzzyExtent) {
                        correctInputCount++;
                        break;
                    }
                }
            }

            FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
            if (correctInputCount>=1) {
                postsRank.put(p,
                        postsRank.getOrDefault(p, 0) + fs.fuzzyScore(p.getContent(), input) - Math.round(fuzzyScore) + onlyExcludeTag);

            }
        }
    }


}
