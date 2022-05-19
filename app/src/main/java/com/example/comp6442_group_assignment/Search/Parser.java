package com.example.comp6442_group_assignment.Search;

import com.example.comp6442_group_assignment.Search.Tags.AuthorTag;
import com.example.comp6442_group_assignment.Search.Tags.ExactTag;
import com.example.comp6442_group_assignment.Search.Tags.ExcludeTag;
import com.example.comp6442_group_assignment.Search.Tags.SharpTag;
import com.example.comp6442_group_assignment.Search.Tags.Tag;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchStringTokenizer;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchToken;

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
     * Parse tags from the tokens.
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

    /**
     * Parse the word from the tokens.
     * @return String as search query.
     */
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
