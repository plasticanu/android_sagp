package com.example.comp6442_group_assignment.Search.Tags;

public class AuthorTag extends Tag{
    String expression;

    public AuthorTag(String s){
        this.expression = s;
    }
    @Override
    public void show() {
        System.out.println("@" + expression);
    }

    @Override
    public Tag evaluate() {
        return new AuthorTag(expression);
    }
}
