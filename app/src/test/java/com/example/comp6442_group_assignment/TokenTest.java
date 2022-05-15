package com.example.comp6442_group_assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import com.example.comp6442_group_assignment.Search.Tokenizer.PostTokenizer;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchStringTokenizer;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchToken;

import org.junit.Test;

import java.util.List;

public class TokenTest {



    String test = "Hello world. @test, #test, \\test, 'test'";
    SearchStringTokenizer searchStringTokenizer = new SearchStringTokenizer(test);

    @Test
    public void searchStringTokenizerTest() {
        String test = "Hello world. @test, #test, \\test, 'test'";
        SearchStringTokenizer searchStringTokenizer = new SearchStringTokenizer(test);

        assertEquals("Hello" ,searchStringTokenizer.getCurrentToken().getToken());
        assertEquals(SearchToken.Type.Word, searchStringTokenizer.getCurrentToken().getType());
        searchStringTokenizer.next();
        assertEquals("world" ,searchStringTokenizer.getCurrentToken().getToken());
        assertEquals(SearchToken.Type.Word, searchStringTokenizer.getCurrentToken().getType());
        searchStringTokenizer.next();
        assertEquals("@test" ,searchStringTokenizer.getCurrentToken().getToken());
        assertEquals(SearchToken.Type.AuthorTag ,searchStringTokenizer.getCurrentToken().getType());
        searchStringTokenizer.next();
        assertEquals("#test" ,searchStringTokenizer.getCurrentToken().getToken());
        assertEquals(SearchToken.Type.HashTag ,searchStringTokenizer.getCurrentToken().getType());
        searchStringTokenizer.next();
        assertEquals("\\test" ,searchStringTokenizer.getCurrentToken().getToken());
        assertEquals(SearchToken.Type.ExcludeTag ,searchStringTokenizer.getCurrentToken().getType());
        searchStringTokenizer.next();
        assertEquals("'test'" ,searchStringTokenizer.getCurrentToken().getToken());
        assertEquals(SearchToken.Type.ExactTag ,searchStringTokenizer.getCurrentToken().getType());
        searchStringTokenizer.next();
        assertFalse(searchStringTokenizer.hasNext());
    }

    @Test
    public void postTokenizerTest() {
        String test = "Hello world, this is a test post.\n";
        Post post = new Post(test, "test_user");
        PostTokenizer tokenizer = new PostTokenizer(post);
        List<String> tokens = tokenizer.getAllTokens();

        assertEquals("test_user", tokens.get(0));
        assertEquals("Hello", tokens.get(1));
        assertEquals("world", tokens.get(2));
        assertEquals("this", tokens.get(3));
        assertEquals("is", tokens.get(4));
        assertEquals("a", tokens.get(5));
        assertEquals("test", tokens.get(6));
        assertEquals("post", tokens.get(7));
    }
}
