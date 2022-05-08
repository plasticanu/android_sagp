package com.example.comp6442_group_assignment.Search.Tags;

public class SharpTag extends Tag{
    String expression;

    public SharpTag(String s){
        this.expression = s;
    }
    @Override
    public void show() {
        String starter = "#";
        System.out.println(starter + expression);
    }

    @Override
    public Tag evaluate() {
        return new SharpTag(expression);
    }
}
