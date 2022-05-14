package com.example.comp6442_group_assignment.Search.Tokenizer;

import com.example.comp6442_group_assignment.Post;
import com.example.comp6442_group_assignment.User;

import java.util.ArrayList;
import java.util.List;

/**
 * This method is used to tokenize the post content. The tokenizer comes with a method of getAllTokens() which will
 * return the whole token list of the post starting with the author.
 */
public class PostTokenizer extends Tokenizer {
    private String content; // The content of the post.
    private String author; // The author of the post.

    private String currentToken; // The current token.
    private int currentIndex; // The current index.

    static final char[] tokenEnd = { ' ', '\n', '\t', '.' , '?' , '!', ',', ';', ':'};

    public PostTokenizer(Post post) {
        content = post.getContent();
        author = post.getAuthor();
        this.currentIndex = 0;
        currentToken = author; // Initialize currentToken to author.
    }

    @Override
    public boolean hasNext() {
        return currentToken != null;
    }

    @Override
    public Object current() {
        return currentToken;
    }

    @Override
    public void next() {
        content = content.trim(); // trim the searchString
        if (content.isEmpty()) { // if searchString is empty, nothing left
            currentToken = null; // set currentToken to null
            return;
        }
        char firstChar = content.charAt(0);
        if (isEnd(firstChar)) { // if firstChar is end of token
            content = content.substring(1); // remove firstChar from searchString
            next();
            return;
        }
        currentToken = getTokenString();
        content = content.substring(currentToken.length()); // update searchString
        currentIndex = 0; // reset currentIndex
    }


    /**
     * Get the token string till a char that is in tokenEnd
     * @return
     */
    private String getTokenString() {
        StringBuilder tokenValue = new StringBuilder();
        while (currentIndex < content.length() && !isEnd(content.charAt(currentIndex))) {
            tokenValue.append(content.charAt(currentIndex));
            currentIndex++;
        }
        return tokenValue.toString();
    }


    /**
     * Check if the character is in the end array
     * @param c
     * @return true if the character is in the array
     */
    private boolean isEnd(char c) {
        for (char w : SearchStringTokenizer.tokenEnd) {
            if (w == c)
                return true;
        }
        return false;
    }

    /**
     * Get all tokens from the post
     * @return a list of tokens starting with author
     */
    public List<String> getAllTokens() {
        List<String> tokens = new ArrayList<>();
        while (hasNext()) {
            tokens.add((String) current());
            next();
        }
        return tokens;
    }

    public static void main(String[] args) {
        String content = "Hello, I am a test post.\n";
        Post post = new Post(content, "test_user");
        PostTokenizer tokenizer = new PostTokenizer(post);
        List<String> tokens = tokenizer.getAllTokens();
        for (String token : tokens) {
            System.out.println(token);
        }
    }
}
