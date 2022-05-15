package com.example.comp6442_group_assignment;

import static org.junit.Assert.assertEquals;

import com.example.comp6442_group_assignment.Search.Search;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class SearchTest {
    Search search = new Search(30);

    @Test
    public void testSearch() throws ParserConfigurationException, IOException, SAXException {
        // test searching with words
        List<Post> search1 =  search.search("1606");
        assertEquals("00000002", search1.get(0).getPostId());
        List<Post> search2 =  search.search("malevolence");
        assertEquals("00000319", search2.get(0).getPostId());
        List<Post> search3 =  search.search("Tarquin");
        assertEquals("00000141", search3.get(1).getPostId());

        // test searching with phrases
        List<Post> search4 =  search.search("surfeited grooms");
        assertEquals("00000145", search4.get(0).getPostId());
        List<Post> search5 =  search.search("good Fleance");
        assertEquals("00000267", search5.get(1).getPostId());
//        List<Post> search6 =  search.search("Tarquin");
//        assertEquals("00000141", search6.get(1).getPostId());


    }

    @Test
    public void testSearchPhrases() throws ParserConfigurationException, IOException, SAXException {
        List<Post> firstPostSearch =  search.search("This is the very first content of the app.");
        assertEquals("00000000", firstPostSearch.get(0).getPostId());


    }

    @Test
    public void testSearchSentences() throws ParserConfigurationException, IOException, SAXException {
        List<Post> firstPostSearch =  search.search("This is the very first content of the app.");
        assertEquals("00000000", firstPostSearch.get(0).getPostId());


    }



}
