package com.example.comp6442_group_assignment;

import static org.junit.Assert.assertEquals;

import com.example.comp6442_group_assignment.Search.SentenceTokenizer;

import org.junit.Test;

public class SimpleParserTest {
    @Test(timeout = 1000)
    public void testParser(){
        String a = "1+1";
        String b = "2+4";
        String c = "a+2";
        String d = "a-2";
        SentenceTokenizer st = new SentenceTokenizer("1+1");
        assertEquals(a, st.parseSentence(st));
    }



}
