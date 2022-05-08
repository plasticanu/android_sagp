package com.example.comp6442_group_assignment.Search.Tags;

public class ExcludeTag extends Tag{
    String expression;
    public ExcludeTag(String s){
        this.expression = s;
    }
    @Override
    public void show() {
        System.out.println("/" + expression);
    }

    @Override
    public Tag evaluate() {
        return new ExcludeTag(expression);
    }
}
