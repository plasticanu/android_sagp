package com.example.comp6442_group_assignment.Search;

public class StringTokenizer {

    private final String text;
    private int pos;
    private String current;

    static final char[] whitespace = { ' ', '\n', '\t' };
    static final char[] sentenceEnd = { '.' , '?' , '!' , '\n', '\t' };
    static final char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    static final char[] mathematicalSymbol = { '+', '-', '*', '/', '%' , '=', '<', '>', '!', '&', '|', '^', '~' };
    static final char[] punctuation = { '.', ',', ';', ':', '?', '!' };
    static final char[] separator = { '(', ')', '[', ']', '{', '}' };

    public StringTokenizer(String text) {
        this.text = text;
        this.pos = 0;
        next();
    }

    public boolean hasNext() {
        return current != null;
    }

    public String current() {
        return current;
    }

    public void next() {
        consumeWhite(); // Remove white space
        if (pos >= text.length()) {
            current = null; // End of text
        } else {
            int start = pos; // Start of the current token
            while (pos < text.length() && !isin(text.charAt(pos), sentenceEnd)) { // End of sentence
                pos++;
            }
            current = text.substring(start, pos); // Create a string of the current token
            pos++;
        }
    }

    /**
     * Remove white space in a sequence until there's no whitespace in the current position
     */
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
