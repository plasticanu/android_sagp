package com.example.comp6442_group_assignment.Search;

public class SentenceTokenizer extends Tokenizer{

    private final String text;
    private int pos;
    private String current;

    static final char[] whitespace = { ' ', '\n', '\t' };
    static final char[] sentenceEnd = { '.' , '?' , '!' , '\n', '\t' };
    static final char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    static final char[] mathematicalSymbol = { '+', '-', '*', '/', '%' , '=', '<', '>', '!', '&', '|', '^', '~' };
    static final char[] punctuation = { '.', ',', ';', ':', '?', '!' };
    static final char[] separator = { '(', ')', '[', ']', '{', '}' };

    public SentenceTokenizer(String text) {
        this.text = text;
        this.pos = 0;
        next();
    }

    @Override
    public boolean hasNext() {
        return current != null;
    }

    @Override
    public String current() {
        return current;
    }

    @Override
    public void next() {
        consumeWhite(); // Remove white space
        if (pos >= text.length()) {
            current = null; // End of text
        } else if (isin(text.charAt(pos), mathematicalSymbol) && isin(text.charAt(pos), separator)) {
            current = String.valueOf(text.charAt(pos)); // Keep math symbols and separators
            pos++; // Move to next position
        }
        else {
            int start = pos;
            while (pos < text.length() && !isin(text.charAt(pos), sentenceEnd) && !isin(text.charAt(pos), whitespace) && !isin(text.charAt(pos), mathematicalSymbol) && !isin(text.charAt(pos), punctuation) && !isin(text.charAt(pos), separator)) { // End of sentence
                pos++;
            }
            current = text.substring(start, pos); // Create a string of the current token
            if (!isin(text.charAt(pos), mathematicalSymbol) && !isin(text.charAt(pos), separator)) { // Keep math symbols and separators
                pos++;
            }
        }
    }

    private void consumeWhite() {
        while (pos < text.length() && isin(text.charAt(pos), whitespace))
            pos++;
    }

    private boolean isin(char c, char[] charList) {
        for (char w : charList) {
            if (w == c)
                return true;
        }
        return false;
    }
}
