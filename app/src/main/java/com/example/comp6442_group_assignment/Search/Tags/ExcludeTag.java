package com.example.comp6442_group_assignment.Search.Tags;

public class ExcludeTag extends Tag{
    String expression;
    public ExcludeTag(String s){
        this.expression = s;
    }
    @Override
    public String show() {
        System.out.println("/" + expression);
        return null;
    }

    @Override
    public Tag evaluate() {
        return new ExcludeTag(expression);
    }
}
