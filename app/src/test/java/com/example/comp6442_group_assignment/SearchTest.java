package com.example.comp6442_group_assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.comp6442_group_assignment.Search.Search;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

public class SearchTest {
    Search search;

    {
        try {
            search = new Search(30);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void fileReaderTest() throws ParserConfigurationException, IOException, SAXException {
        // test file reading methods
        List<User> users = User.readUsers();
        assertEquals("user1", users.get(0).getUserName());
        assertEquals("user3", users.get(1).getUserName());
        assertEquals("user4", users.get(2).getUserName());
        List<Post> posts = Post.readFromPost();
        assertEquals("00000000", posts.get(0).getPostId());
        assertEquals("00000001", posts.get(1).getPostId());
        assertEquals("00000002", posts.get(2).getPostId());
    }

    @Test
    public void testSearchWords() throws ParserConfigurationException, IOException, SAXException {

        assertThrows(NullPointerException.class, () -> {
            search.search(null);
        });
        // test searching with words
        List<Post> search1 = search.search("Personae");
        assertEquals("00000005", search1.get(0).getPostId());
        List<Post> search2 = search.search("malevolence");
        assertEquals("00001587", search2.get(0).getPostId());
        List<Post> search3 = search.search("Tarquin");
        assertEquals("00000698", search3.get(0).getPostId());
        List<Post> search4 = search.search("perseverance");
        assertEquals("00002020", search4.get(0).getPostId());
        List<Post> search5 = search.search("scepter");
        assertEquals("00002033", search5.get(0).getPostId());
        List<Post> search6 = search.search("ripe");
        assertEquals("00002193", search6.get(0).getPostId());
        List<Post> search7 = search.search("herald");
        assertEquals("00000254", search7.get(0).getPostId());
        List<Post> search8 = search.search("hangman");
        assertEquals("00000749", search8.get(0).getPostId());
        List<Post> search9 = search.search("sleepers");
        assertEquals("00000913", search9.get(0).getPostId());
        List<Post> search10 = search.search("unfold");
        assertEquals("00001607", search10.get(0).getPostId());
    }

    @Test
    public void testSearchPhrases() throws ParserConfigurationException, IOException, SAXException {
        // test searching with phrases
        List<Post> search1 = search.search("suffering country");
        assertEquals("00001609", search1.get(0).getPostId());
        List<Post> search2 = search.search("violent hands");
        assertEquals("00002660", search2.get(0).getPostId());
        List<Post> search3 = search.search("Earl of Northumberland");
        assertEquals("00000021", search3.get(0).getPostId());
        List<Post> search4 = search.search("Son of Macduff");
        assertEquals("00000026", search4.get(0).getPostId());
        List<Post> search5 = search.search("porter of Hell");
        assertEquals("00000809", search5.get(0).getPostId());
        List<Post> search6 = search.search("an equivocator");
        assertEquals("00000817", search6.get(0).getPostId());
        List<Post> search7 = search.search("English tailor");
        assertEquals("00000822", search7.get(0).getPostId());
        List<Post> search8 = search.search("three Murtherers");
        assertEquals("00001300", search8.get(0).getPostId());
        List<Post> search9 = search.search("near approaches");
        assertEquals("00001309", search9.get(0).getPostId());
        List<Post> search10 = search.search("scorn death");
        assertEquals("00001546", search10.get(0).getPostId());
    }

    @Test
    public void testSearchSentences() throws ParserConfigurationException, IOException, SAXException {
        // test searching with sentences
        List<Post> search1 = search.search("This is the very first content of the app");
        assertEquals("00000000", search1.get(0).getPostId());
        List<Post> search2 = search.search("Whom we invite to see us crown'd at Scone");
        assertEquals("00002665", search2.get(0).getPostId());
        List<Post> search3 = search.search("Hear it not, Duncan, for it is a knell");
        assertEquals("00000707", search3.get(0).getPostId());
        List<Post> search4 = search.search("DUNCAN. Is execution done on Cawdor?");
        assertEquals("00000325", search4.get(0).getPostId());
        List<Post> search5 = search.search("Became him like the leaving it");
        assertEquals("00000333", search5.get(0).getPostId());
        List<Post> search6 = search.search("he grows worse and worse;");
        assertEquals("00001482", search6.get(0).getPostId());
        List<Post> search7 = search.search("A kind good night to all!");
        assertEquals("00001488", search7.get(0).getPostId());
        List<Post> search8 = search.search("The secret'st man of blood");
        assertEquals("00001494", search8.get(0).getPostId());
        List<Post> search9 = search.search("By the worst means, the worst.");
        assertEquals("00001504", search9.get(0).getPostId());
        List<Post> search10 = search.search("I am so much a fool, should I stay longer");
        assertEquals("00001844", search10.get(0).getPostId());
    }

    @Test
    public void testMultipleResults() throws ParserConfigurationException, IOException, SAXException {
        // test vague searches
        List<Post> search1 = search.search("SECOND MURTHERER");
        List<String> postIDs_1 = new ArrayList<>();
        for (Post post : search1) postIDs_1.add(post.getPostId());
        assertTrue(postIDs_1.contains("00001303"));
        assertTrue(postIDs_1.contains("00001190"));
        assertTrue(postIDs_1.contains("00001212"));
        assertTrue(postIDs_1.contains("00001313"));
        assertTrue(postIDs_1.contains("00001320"));
        assertTrue(postIDs_1.contains("00001332"));

        List<Post> search2 = search.search("son of Duncan");
        List<String> postIDs_2 = new ArrayList<>();
        for (Post post : search2) postIDs_2.add(post.getPostId());
        assertTrue(postIDs_2.contains("00000012"));
        assertTrue(postIDs_2.contains("00000013"));
        assertTrue(postIDs_2.contains("00001583"));

        List<Post> search3 = search.search("apparition");
        List<String> postIDs_3 = new ArrayList<>();
        for (Post post : search3) postIDs_3.add(post.getPostId());
        assertTrue(postIDs_3.contains("00001713"));
        assertTrue(postIDs_3.contains("00001702"));
        assertTrue(postIDs_3.contains("00001725"));
        assertTrue(postIDs_3.contains("00000033"));
    }


    @Test
    public void testAuthorTag() throws ParserConfigurationException, IOException, SAXException {
        List<Post> result = search.search("@user1");
        //2678 result because fuzzy search. user2, user3 etc are also in the result.
        assertEquals(2678, result.size());

        int user1Number = 0;
        for (Post p : result) {
            if (p.getAuthor().equals("user1")) {
                user1Number++;
            } else {
                break;
            }
        }
        // user1 posts have 897
        assertEquals(898, user1Number);

    }

    @Test
    public void testExactTag() throws ParserConfigurationException, IOException, SAXException {
        // test searching with exact tag
        List<Post> result = search.search("'this'");
        assertEquals(83, result.size());

        int correctResultCount = 0;
        for (Post p : result) {
            if (p.getContent().toLowerCase().contains("this")) {
                correctResultCount++;
            } else {
                break;
            }
        }
        assertEquals(83, correctResultCount);

    }

    @Test
    public void testHashTag() throws ParserConfigurationException, IOException, SAXException {
        // test searching with hash tag
        List<Post> result = search.search("#you");

        assertEquals(1, result.size());
        assertEquals("00000895", result.get(0).getPostId());
    }

    @Test
    public void testContentWithTag() throws ParserConfigurationException, IOException, SAXException {
        // test searching content with tag
        List<Post> result = search.search("@user1, this is a");
        int expected = 0;
        for (Post post: result) {
            if (post.getAuthor().equals("user1") && post.getContent().contains("this is a"));
            expected ++;
        }
        assertEquals(expected, result.size());
    }

}
