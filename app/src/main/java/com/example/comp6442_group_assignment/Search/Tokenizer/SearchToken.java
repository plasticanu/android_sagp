package com.example.comp6442_group_assignment.Search.Tokenizer;

import org.jetbrains.annotations.NotNull;

/**
 * SearchToken class is used to store the token and its type from the search string.
 */
public class SearchToken {
    public enum Type {Word, AuthorTag, ExactTag, ExcludeTag, HashTag}

    private String token;
    private Type type;

    public SearchToken(String token, Type type) {
        this.token = token;
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public Type getType() {
        return type;
    }

    public String getString() {
        String rtn = "";
        if (type == Type.Word) {
            rtn = token;
        } else if (type == Type.ExactTag) {
            rtn = token.substring(1, token.length() - 1);
        } else {
            rtn = token.substring(1);
        }
        return rtn;
    }

    @NotNull
    @Override
    public String toString() {
        return "SearchToken{" +
                "token='" + token + '\'' +
                ", type=" + type +
                '}';
    }
}
