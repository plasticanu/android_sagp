package com.example.comp6442_group_assignment;

import static org.junit.Assert.assertEquals;

import com.example.comp6442_group_assignment.Search.Parser;
import com.example.comp6442_group_assignment.Search.Tags.Tag;
import com.example.comp6442_group_assignment.Search.Tokenizer.SearchStringTokenizer;

import org.junit.Test;

import java.util.ArrayList;

public class PaserTest {
    @Test
    public void parseTagsTest(){
        // test author tags
        String authorTest = "Hello world, this is written by @John, @Kevin and @Steve.";
        SearchStringTokenizer tokenizer = new SearchStringTokenizer(authorTest);
        Parser parser = new Parser(tokenizer);
        ArrayList<Tag> tags = parser.parseTags();
        assertEquals("John", tags.get(0).evaluate());
        assertEquals("A", tags.get(0).getType());
        assertEquals("Kevin", tags.get(1).evaluate());
        assertEquals("Steve", tags.get(2).evaluate());

        // test exact tags
        String exactTest = "Hello world, this is written by 'John', 'Kevin' and 'Steve'.";
        tokenizer = new SearchStringTokenizer(exactTest);
        parser = new Parser(tokenizer);
        ArrayList<Tag> tags2 = parser.parseTags();
        assertEquals("John", tags2.get(0).evaluate());
        assertEquals("E", tags2.get(0).getType());
        assertEquals("Kevin", tags2.get(1).evaluate());
        assertEquals("Steve", tags2.get(2).evaluate());

        // test exclude tags
        String excludeTag = "Hello world, this is written by \\John, \\Kevin and \\Steve.";
        tokenizer = new SearchStringTokenizer(excludeTag);
        parser = new Parser(tokenizer);
        ArrayList<Tag> tags3 = parser.parseTags();
        assertEquals("John", tags3.get(0).evaluate());
        assertEquals("Ex", tags3.get(0).getType());
        assertEquals("Kevin", tags3.get(1).evaluate());
        assertEquals("Steve", tags3.get(2).evaluate());

        // test exclude tags
        String sharpTag = "Hello world, this is written by #John, #Kevin and #Steve.";
        tokenizer = new SearchStringTokenizer(sharpTag);
        parser = new Parser(tokenizer);
        ArrayList<Tag> tags4 = parser.parseTags();
        assertEquals("John", tags4.get(0).evaluate());
        assertEquals("S", tags4.get(0).getType());
        assertEquals("Kevin", tags4.get(1).evaluate());
        assertEquals("Steve", tags4.get(2).evaluate());

        // test for empty case
        String emptyTest = "";
        tokenizer = new SearchStringTokenizer(emptyTest);
        parser = new Parser(tokenizer);
        ArrayList<Tag> emptyTag = parser.parseTags();
        assertEquals(new ArrayList<Tag>(), emptyTag);
    }

    @Test
    public void parseWordTest(){
        // test for simple cases
        String test1 = "Hello world, this is a test.";
        SearchStringTokenizer tokenizer = new SearchStringTokenizer(test1);
        Parser parser = new Parser(tokenizer);
        assertEquals(" Hello world this is a test", parser.parseWord());

        String test2 = "Is today Friday, Saturday or Sunday?";
        tokenizer = new SearchStringTokenizer(test2);
        parser = new Parser(tokenizer);
        assertEquals(" Is today Friday Saturday or Sunday", parser.parseWord());

        // test for punctuations
        String test3 = "Hello world, @test, #test.";
        tokenizer = new SearchStringTokenizer(test3);
        parser = new Parser(tokenizer);
        assertEquals(" Hello world", parser.parseWord());

        String test4 = "Hello$$ people)";
        tokenizer = new SearchStringTokenizer(test4);
        parser = new Parser(tokenizer);
        assertEquals(" Hello people", parser.parseWord());

        String test5 = "How%%are you";
        tokenizer = new SearchStringTokenizer(test5);
        parser = new Parser(tokenizer);
        assertEquals(" How are you", parser.parseWord());

        // test for empty case
        String test6 = "";
        tokenizer = new SearchStringTokenizer(test6);
        parser = new Parser(tokenizer);
        assertEquals(" ", parser.parseWord());
    }
}
