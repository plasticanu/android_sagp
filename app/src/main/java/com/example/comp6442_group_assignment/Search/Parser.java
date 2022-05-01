package com.example.comp6442_group_assignment.Search;

import java.util.Collection;
import java.util.Collections;

/**
 * Tag grammar:
 * Tag = #String
 */
public class Parser {
    static final char[] whitespace = { ' ', '\n', '\t' };
    Tokenizer tokenizer;
    public Parser(Tokenizer tokenizer){
        this.tokenizer = tokenizer;
    }

    public Tag parseTag(){
        if(tokenizer.current().toString().charAt(0) == '#'){

            return new SharpTag(tokenizer.current().toString());
        }

        return null;
    }
}
