package com.example.comp6442_group_assignment.Search.Tags;

public class ExactTag extends Tag{
    String expression;
    public ExactTag(String s){
        this.expression = s;

    }

    @Override
    public String show() {
        System.out.println("'" + expression + "'");
        return null;
    }

    @Override
    public String evaluate() {
        return expression;
    }

    @Override
    public String getType() {
        return "E";
    }
}
