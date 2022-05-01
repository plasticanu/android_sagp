package com.example.comp6442_group_assignment.Search;

public class SharpTag extends Tag{
    private String expression;

    public SharpTag(String s){
        String starter = "#";
        this.expression = starter + s;
    }
    @Override
    public String show() {
        return expression;
    }

    @Override
    public Tag evaluate() {
        return new SharpTag(expression);
    }
}
