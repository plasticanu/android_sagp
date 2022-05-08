package com.example.comp6442_group_assignment.Search;

import com.example.comp6442_group_assignment.Search.Tags.AuthorTag;
import com.example.comp6442_group_assignment.Search.Tags.ExactMathTag;
import com.example.comp6442_group_assignment.Search.Tags.ExcludeTag;
import com.example.comp6442_group_assignment.Search.Tags.SharpTag;
import com.example.comp6442_group_assignment.Search.Tags.Tag;

import java.util.ArrayList;

/**
 * Grammar analyzer for the search engine.
 * @ search for author
 * # search for tag
 * / exclude, must not contain
 * ' ' exact match, must contain
 */
public class Parser {
    public static class IllegalProductionException extends IllegalArgumentException {
        public IllegalProductionException(String errorMessage) {
            super(errorMessage);
        }
    }
    Tokenizer tokenizer;

    public Parser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }
    public static void main(String[] args) {
        String test = "@abc #araworij 143124 @abcccc 'awwad'";
        SentenceTokenizer s = new SentenceTokenizer(test);
        Parser p = new Parser(s);
        for(Tag t : p.parseTag()){
            t.show();
        }

    }

    /**
     * Parse tags from the input. It reads all the tags once.
     * The result is an ArrayList contain the tags.
     * @return ArrayList<Tag>
     */
    public ArrayList<Tag> parseTag(){
        // Declare the result to be an Arraylist
        ArrayList<Tag> result = new ArrayList<>();

        // The loop reads all tags until there are no tokens left.
        while(tokenizer.hasNext()) {
            // Tag recognizing
            String tagStarter = tokenizer.current().toString();
            // if the first char is '
            if((int) tagStarter.charAt(0) == 39 && tagStarter.length()>2){
                // if the tail char is '
                if((int) tagStarter.charAt(tagStarter.length() - 1) == 39){
                    // Recognized exactMathTag
                    String exactMathTag = tagStarter.substring(1, tagStarter.length()-1);
                    result.add(new ExactMathTag(exactMathTag));
                }
            }
            // Recognize other cases: # @ /
            else {
                switch (tagStarter) {
                    case "#":
                        tokenizer.next();
                        String tag = tokenizer.current().toString();
                        result.add(new SharpTag(tag));
                        break;
                    case "@":
                        tokenizer.next();
                        String authorTag = tokenizer.current().toString();
                        result.add(new AuthorTag(authorTag));
                        break;
                    case "/":
                        tokenizer.next();
                        String excludeTag = tokenizer.current().toString();
                        result.add(new ExcludeTag(excludeTag));
                        break;
                }
            }
            if(tokenizer.hasNext()){
                tokenizer.next();
            }
        }
        return result;
    }
}
