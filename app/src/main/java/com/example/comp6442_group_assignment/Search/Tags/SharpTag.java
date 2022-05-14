package com.example.comp6442_group_assignment.Search.Tags;

public class SharpTag extends Tag{
    String expression;

    public SharpTag(String s){
        this.expression = s;
    }
    @Override
    public String show() {
        String starter = "#";
        System.out.println(starter + expression);
        return starter;
    }

    @Override
    public String evaluate() {
        return expression;
    }

    @Override
    public String getType() {
        return "S";
    }
}
