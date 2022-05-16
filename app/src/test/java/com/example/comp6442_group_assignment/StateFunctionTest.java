package com.example.comp6442_group_assignment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.comp6442_group_assignment.State.GuestState;
import com.example.comp6442_group_assignment.State.LoggedInState;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class StateFunctionTest {
    private UserSession userSession;

    @Test
    public void testState() throws ParserConfigurationException, IOException, SAXException {
        userSession = new UserSession();
        assertEquals(userSession.getState().getClass(), GuestState.class);
        assertEquals(userSession.getUserName(), "Guest");
        assertFalse(userSession.isLoggedIn);
        assertNull(userSession.user);
    }

    @Test
    public void testLogin() throws ParserConfigurationException, IOException, SAXException {
        userSession = new UserSession();
        // test login with wrong password
        assertFalse(userSession.login("test", "test"));
        assertEquals(userSession.getState().getClass(), GuestState.class);
        assertEquals(userSession.getUserName(), "Guest");
        assertFalse(userSession.isLoggedIn);
        assertNull(userSession.user);
        // test login with correct password
        assertTrue(userSession.login("user1", "qwerty"));
        assertEquals(userSession.getState().getClass(), LoggedInState.class);
        assertEquals(userSession.getUserName(), "user1");
        assertTrue(userSession.isLoggedIn);
        assertNotNull(userSession.user);
    }

    @Test
    public void testLogout() throws ParserConfigurationException, IOException, SAXException {
        userSession = new UserSession();
        // test logout when not logged in
        assertFalse(userSession.logout());
        assertEquals(userSession.getState().getClass(), GuestState.class);
        assertEquals(userSession.getUserName(), "Guest");
        assertFalse(userSession.isLoggedIn);
        assertNull(userSession.user);
        // test logout when logged in
        assertTrue(userSession.login("user1", "qwerty"));
        assertTrue(userSession.logout());
        assertEquals(userSession.getState().getClass(), GuestState.class);
        assertEquals(userSession.getUserName(), "Guest");
        assertFalse(userSession.isLoggedIn);
        assertNull(userSession.user);
    }

    @Test
    public void testRegister() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        userSession = new UserSession();
        // test register with existing username
        assertFalse(userSession.register("user1", "test", "test", "test", "test", "test"));
        assertEquals(userSession.getState().getClass(), GuestState.class);
        assertEquals(userSession.getUserName(), "Guest");
        assertFalse(userSession.isLoggedIn);
        assertNull(userSession.user);
        // test register with incorrect username and password
        assertFalse(userSession.register("te", "test", "test", "test", "test", "test"));
        assertEquals(userSession.getState().getClass(), GuestState.class);
        assertEquals(userSession.getUserName(), "Guest");
        assertFalse(userSession.isLoggedIn);
        assertNull(userSession.user);
        // test register with correct username and password
        assertTrue(userSession.register("user2", "qwerty", "user2@mail.com", "First2", "Last2", "123456789"));
        assertEquals(userSession.getState().getClass(), LoggedInState.class);
        assertEquals(userSession.getUserName(), "user2");
        assertTrue(userSession.isLoggedIn);
        assertNotNull(userSession.user);
        User expected = new User("user2", "qwerty", "user2@mail.com", "First2", "Last2", "123456789", new ArrayList<>(), true);
        assertEquals(userSession.user.toString(), expected.toString());
    }

    @Test
    public void testUpdateProfile() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        userSession = new UserSession();
        // test update profile when not logged in
        userSession.updateProfile("user2", "qwertyuiop", "user2@mail.com", "First2", "Last2", "123456789");
        assertEquals(userSession.getState().getClass(), GuestState.class);
        assertEquals(userSession.getUserName(), "Guest");
        assertFalse(userSession.isLoggedIn);
        assertNull(userSession.user);
        // test update profile when logged in
        assertTrue(userSession.login("user2", "qwerty"));
        userSession.updateProfile("user2", "qwertyuiop", "user2@mail.com", "First2", "Last2", "123456789");
        assertEquals(userSession.getState().getClass(), LoggedInState.class);
        assertEquals(userSession.getUserName(), "user2");
        assertTrue(userSession.isLoggedIn);
        assertNotNull(userSession.user);
        User expected = new User("user2", "qwertyuiop", "user2@mail.com", "First2", "Last2", "123456789", new ArrayList<>(), true);
        assertEquals(userSession.user.toString(), expected.toString());
        // update back to original
        userSession.updateProfile("user2", "qwerty", "user2@mail.com", "First2", "Last2", "123456789");
        assertEquals(userSession.getState().getClass(), LoggedInState.class);
        assertEquals(userSession.getUserName(), "user2");
        assertTrue(userSession.isLoggedIn);
        assertNotNull(userSession.user);
        expected = new User("user2", "qwerty", "user2@mail.com", "First2", "Last2", "123456789", new ArrayList<>(), true);
        assertEquals(userSession.user.toString(), expected.toString());
    }

    @Test
    public void testDeleteAccount() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        userSession = new UserSession();
        // test delete account when not logged in
        assertFalse(userSession.deleteAccount());
        assertEquals(userSession.getState().getClass(), GuestState.class);
        assertEquals(userSession.getUserName(), "Guest");
        assertFalse(userSession.isLoggedIn);
        assertNull(userSession.user);
        // test delete account when logged in
        assertTrue(userSession.login("user2", "qwerty"));
        assertTrue(userSession.deleteAccount());
        assertEquals(userSession.getState().getClass(), GuestState.class);
        assertEquals(userSession.getUserName(), "Guest");
        assertFalse(userSession.isLoggedIn);
        assertNull(userSession.user);
    }
    //TODO: to complete this as much as possible, and the other test cases.

    @Test
    public void testCreatePost() throws ParserConfigurationException, IOException, SAXException {
        userSession = new UserSession();
        // test create post when not logged in
        assertNull(userSession.createPost("test post"));

        // test create post when logged in
        userSession.login("user1", "qwerty");
        assertNotNull(userSession.createPost("test post"));

        // test the post's attributes
        Post postCreated = userSession.allPosts().get(userSession.allPosts().size()-1);
        assertEquals("00002668", postCreated.getPostId());
        assertEquals("user1", postCreated.getAuthor());
        assertEquals("test post", postCreated.getContent());

        assertTrue(userSession.deletePost(postCreated.getPostId()));
        assertFalse(userSession.deletePost(postCreated.getPostId()));
    }

    @Test
    public void testUpdatePost() throws ParserConfigurationException, IOException, SAXException {
        userSession = new UserSession();
        userSession.login("user1", "qwerty");
        userSession.createPost("test post");
        Post postCreated = userSession.allPosts().get(userSession.allPosts().size()-1);
        Post otherPost = Post.readFromPost().get(3);

        // test edit post content
        assertTrue(userSession.editPost(postCreated.getPostId(), "I updated this post"));
        Post postEdited = userSession.allPosts().get(userSession.allPosts().size()-1);
        assertEquals("I updated this post", postEdited.getContent());
        assertFalse(userSession.editPost("00002667", "cannot edit")); // test edit other people's post

        // test like and unlike post
        assertFalse(userSession.likePost(postCreated.getPostId())); // test like on own post
        assertFalse(userSession.unlikePost(postCreated.getPostId())); // test unlike on own post
        assertTrue(userSession.likePost(otherPost.getPostId()));
        assertFalse(userSession.likePost(otherPost.getPostId())); // test like on liked post
        assertFalse(userSession.likePost("00002669")); // test like on post that doesn't exist
        assertFalse(userSession.unlikePost("00002669")); // test unlike on post that doesn't exist
        Post likedPost = Post.readFromPost().get(3);
        assertEquals(userSession.getUserName(), likedPost.getLikes().get(0));
        assertTrue(userSession.unlikePost(otherPost.getPostId()));
        Post unlikedPost = Post.readFromPost().get(3);
        assertFalse(userSession.unlikePost(unlikedPost.getPostId())); // test unlike on unliked post
        assertEquals(0, unlikedPost.getLikes().size());

        // test comment post
        assertTrue(userSession.commentPost(postCreated.getPostId(), "what a good post!"));
        assertTrue(userSession.commentPost(postCreated.getPostId(), "what a good post, again!")); // test comment twice
        assertFalse(userSession.commentPost("00002669", "where is the post?")); // test comment on post that doesn't exist
        Post commetedPost = userSession.allPosts().get(userSession.allPosts().size()-1);
        assertEquals("what a good post!", commetedPost.getComments().get(0).getContent());
        assertEquals("what a good post, again!", commetedPost.getComments().get(1).getContent());
        assertEquals(userSession.getUserName(), commetedPost.getComments().get(0).getAuthor());

        // test follow and unfollow post
        assertFalse(userSession.followPost(postCreated.getPostId())); // test follow own post
        assertTrue(userSession.followPost(otherPost.getPostId()));
        assertFalse(userSession.followPost(otherPost.getPostId())); // test follow followed post
        assertFalse(userSession.followPost("00002669")); // test follow post that doesn't exist
        assertFalse(userSession.unfollowPost("00002669")); // test unfollow post that doesn't exist
        Post followedPost = Post.readFromPost().get(3);
        assertEquals(userSession.getUserName(), followedPost.getObservers().get(0));
        assertTrue(userSession.unfollowPost(otherPost.getPostId()));
        Post unfollowedPost = Post.readFromPost().get(3);
        assertEquals(0, unfollowedPost.getObservers().size());
        assertFalse(userSession.unfollowPost(otherPost.getPostId())); // test unfollow unfollowed post


        userSession.clearNotifications();
        userSession.deletePost(postCreated.getPostId());
    }

}
