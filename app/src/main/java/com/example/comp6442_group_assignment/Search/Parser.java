package com.example.comp6442_group_assignment.Search;

import com.example.comp6442_group_assignment.Search.Tags.AuthorTag;
import com.example.comp6442_group_assignment.Search.Tags.ExactMathTag;
import com.example.comp6442_group_assignment.Search.Tags.ExcludeTag;
import com.example.comp6442_group_assignment.Search.Tags.SharpTag;
import com.example.comp6442_group_assignment.Search.Tags.Tag;
import com.example.comp6442_group_assignment.Search.Tokenizer.PostTokenizer;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchStringTokenizer;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchToken;
import com.example.comp6442_group_assignment.Search.Tokenizer.Tokenizer;

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


    }

    /**
     * Parse author tags from the input. It reads all the tags once.
     * The result is an ArrayList contain the tags.
     * @return ArrayList<AuthorTag>
     * @Author Zhidong Piao u7139999
     */
    public ArrayList<String> parseAuthorTag() {
        ArrayList<String> result = new ArrayList<>();
        while(tokenizer.hasNext()){
            if (tokenizer.getCurrentToken().getType() == SearchToken.Type.AuthorTag) {
                result.add(tokenizer.getCurrentToken().getString());
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
        return result.toString();
    }
}
