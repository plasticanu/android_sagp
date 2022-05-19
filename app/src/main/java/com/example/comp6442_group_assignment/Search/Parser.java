package com.example.comp6442_group_assignment.Search;

import com.example.comp6442_group_assignment.Search.Tags.AuthorTag;
import com.example.comp6442_group_assignment.Search.Tags.ExactTag;
import com.example.comp6442_group_assignment.Search.Tags.ExcludeTag;
import com.example.comp6442_group_assignment.Search.Tags.SharpTag;
import com.example.comp6442_group_assignment.Search.Tags.Tag;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchStringTokenizer;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchToken;
import org.apache.commons.text.similarity.*;
import java.util.ArrayList;
import java.util.Locale;

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
    SearchStringTokenizer tokenizer;

    public Parser(SearchStringTokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    /**
     * @Author Zhidong Piao u7139999
     * please run with coverage to avoid errors.
     * @param args
     */
    public static void main(String[] args) {
        String input = "@awe #awr 'awrawr @awoirrr i will not hate you";
        SearchStringTokenizer stt = new SearchStringTokenizer(input);
        Parser p = new Parser(stt);
        p.parseTags();
        System.out.println(input.toString().contains("i will"));
        FuzzyScore fs = new FuzzyScore(Locale.ENGLISH);
        System.out.println(fs.fuzzyScore("i will", "a"));
        System.out.println(fs.fuzzyScore("i will drink a cofee", "i cofee"));
        System.out.println(fs.fuzzyScore("the coffee will make you conscious", "you"));
        System.out.println(fs.fuzzyScore("the coffee will make you conscious and not sleepy oh that's amazing", "you"));
        LevenshteinDistance ld = new LevenshteinDistance();
        System.out.println(ld.apply("user1", "user3"));
        ArrayList<String> users = new ArrayList<>();
        for(String s : users){
            System.out.println(s);
        }


    }

    /**
     *
     * @return ArrayList<AuthorTag>
     * @Author Zhidong Piao u7139999
     */
    public ArrayList<Tag> parseTags() {
        ArrayList<Tag> result = new ArrayList<>();
        while(tokenizer.hasNext()){
            switch(tokenizer.getCurrentToken().getType()){
                case AuthorTag:
                    result.add(new AuthorTag(tokenizer.getCurrentToken().getString()));
                    break;
                case ExactTag:
                    result.add(new ExactTag(tokenizer.getCurrentToken().getString()));
                    break;
                case ExcludeTag:
                    result.add(new ExcludeTag(tokenizer.getCurrentToken().getString()));
                    break;
                case HashTag:
                    result.add(new SharpTag(tokenizer.getCurrentToken().getString()));
                    break;
            }
            tokenizer.next();
        }
        return result;
    }

    public String parseWord(){
        StringBuilder result = new StringBuilder();
        while(tokenizer.hasNext()){
            if(tokenizer.getCurrentToken().getType() == SearchToken.Type.Word){
                result.append(" ").append(tokenizer.getCurrentToken().getString());
            }
            tokenizer.next();
        }
        if(result.length() == 0){
            return " ";
        }
        else{
            return result.toString();
        }

    }
}
