package com.example.comp6442_group_assignment.Search.Tags;

public class ExactMathTag extends Tag{
    String expression;
    public ExactMathTag(String s){
        this.expression = s;

    }

    @Override
    public String show() {
        System.out.println("'" + expression + "'");
        return null;
    }

    @Override
    public Tag evaluate() {
        return new ExactMathTag(expression);
    }
}
