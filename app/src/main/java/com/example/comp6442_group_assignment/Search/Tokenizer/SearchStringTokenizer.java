package com.example.comp6442_group_assignment.Search.Tokenizer;

public class SearchStringTokenizer extends Tokenizer {
    private String searchString;
    private SearchToken currentToken;
    private int currentIndex;

    static final char[] tokenEnd = { ' ', '\n', '\t', '.' , '?' , '!', ',', ';', ':', '"', '/', '|', '_', '-', '+', '=', '~', '`', '$', '%', '^', '&', '*', '(', ')', '{', '}', '[', ']', '<', '>', '\r', '\f' };

    public SearchStringTokenizer(String searchString) {
        this.searchString = searchString;
        this.currentIndex = 0;
        next(); // initialize currentToken
    }

    @Override
    public boolean hasNext() {
        return currentToken != null; // return null if currentToken is null
    }

    @Override
    public Object current() {
        return currentToken; // return currentToken
    }

    @Override
    public void next() {
        searchString = searchString.trim(); // trim the searchString

        if (searchString.isEmpty()) { // if searchString is empty, nothing left
            currentToken = null; // set currentToken to null
            return;
        }
        char firstChar = searchString.charAt(0);
        if (isEnd(firstChar)) { // if firstChar is end of token
            searchString = searchString.substring(1); // remove firstChar from searchString
            next();
            return;
        }
        if (firstChar == '@') {
            currentToken = new SearchToken(getTokenString(), SearchToken.Type.AuthorTag);
        } else if (firstChar == '#') {
            currentToken = new SearchToken(getTokenString(), SearchToken.Type.HashTag);
        } else if (firstChar == '\\') {
            currentToken = new SearchToken(getTokenString(), SearchToken.Type.ExcludeTag);
        } else if (firstChar == '\'') {
            boolean paired = false; // ' symbol need to be paired to be exact search
            while (currentIndex < searchString.length()) { // go through the searchString
                currentIndex++;
                if (searchString.charAt(currentIndex) == '\'') { // if paired, set paired to true and break
                    paired = true;
                    break;
                }
            }
            if (paired) { // if paired, set currentToken to exact search token
                currentIndex++;
                currentToken = new SearchToken(searchString.substring(0, currentIndex), SearchToken.Type.ExactTag);
            } else { // if not paired, set currentToken to a default word token
                currentIndex = 0;
                currentToken = new SearchToken(getTokenString(), SearchToken.Type.Word);
            }
        }
        else {
            currentToken = new SearchToken(getTokenString(), SearchToken.Type.Word);
        }
        searchString = searchString.substring(currentToken.getToken().length()); // update searchString
        currentIndex = 0; // reset currentIndex
    }

    /**
     * Get the token string till a char that is in tokenEnd
     * @return
     */
    private String getTokenString() {
        StringBuilder tokenValue = new StringBuilder();
        while (currentIndex < searchString.length() && !isEnd(searchString.charAt(currentIndex))) {
            tokenValue.append(searchString.charAt(currentIndex));
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

    public static void main(String[] args) {
        String test = "Hello world, this is a test. @test, #test, \\test, 'test'";
        SearchStringTokenizer tokenizer = new SearchStringTokenizer(test);
        int counter = 0;
        while (tokenizer.hasNext() && counter < 10) {
            System.out.println(tokenizer.currentToken.getToken() + "--" + tokenizer.currentToken.getType() + "--" + tokenizer.currentToken.getString());
            tokenizer.next();
            counter++;
        }
    }
}
