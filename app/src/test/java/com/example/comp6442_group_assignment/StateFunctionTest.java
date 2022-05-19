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
import java.util.List;

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
        assertNull(userSession.profile());
        assertNull(userSession.requestProfile("user1"));
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
        assertFalse(userSession.login("user1", "qwerty"));
        assertEquals(userSession.getState().getClass(), LoggedInState.class);
        assertEquals(userSession.getUserName(), "user1");
        assertTrue(userSession.isLoggedIn);
        assertNotNull(userSession.user);
        List<User> users = User.readUsers();
        assertEquals(users.get(2).getUserName(), userSession.requestProfile("user4").getUserName());
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
        assertFalse(userSession.updateProfile("user2", "qwertyuiop", "user2@mail.com", "First2", "Last2", "123456789", true));
        assertEquals(userSession.getState().getClass(), GuestState.class);
        assertEquals(userSession.getUserName(), "Guest");
        assertFalse(userSession.isLoggedIn);
        assertNull(userSession.user);
        // test update profile when logged in
        assertTrue(userSession.login("user2", "qwerty"));
        userSession.updateProfile("user2", "qwertyuiop", "user2@mail.com", "First2", "Last2", "123456789", true);
        assertEquals(userSession.getState().getClass(), LoggedInState.class);
        assertEquals(userSession.getUserName(), "user2");
        assertTrue(userSession.isLoggedIn);
        assertNotNull(userSession.user);
        User expected = new User("user2", "qwertyuiop", "user2@mail.com", "First2", "Last2", "123456789", new ArrayList<>(), true);
        assertEquals(userSession.user.toString(), expected.toString());
        // update back to original
        userSession.updateProfile("user2", "qwerty", "user2@mail.com", "First2", "Last2", "123456789", true);
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

    @Test
    public void testGuestStatePost() throws ParserConfigurationException, IOException, SAXException {
        userSession = new UserSession();
        assertNull(userSession.createPost("test post"));
        assertFalse(userSession.deletePost("00000000"));
        assertFalse(userSession.editPost("00000000", "cannot edit"));
        assertFalse(userSession.likePost("00000000"));
        assertFalse(userSession.unlikePost("00000000"));
        assertFalse(userSession.commentPost("00000000", "cannot comment"));
        assertFalse(userSession.followPost("00000000"));
        assertFalse(userSession.unfollowPost("00000000"));
        assertNull(userSession.allPosts());
        assertNull(userSession.search("macbeth"));
        assertNull(userSession.updateNotifications());
        assertFalse(userSession.clearNotifications());
    }

    @Test
    public void testCreatePost() throws ParserConfigurationException, IOException, SAXException {
        userSession = new UserSession();

        // test create post when logged in
        userSession.login("user1", "qwerty");
        assertNotNull(userSession.createPost("test post"));

        // test the post's attributes
        Post postCreated = userSession.allPosts().get(userSession.allPosts().size()-1);
        assertEquals("user1", postCreated.getAuthor());
        assertEquals("test post", postCreated.getContent());
        assertTrue(Post.exists(postCreated.getPostId()));

        // test delete post
        assertTrue(userSession.deletePost(postCreated.getPostId()));
        assertFalse(Post.exists(postCreated.getPostId()));
        assertFalse(userSession.deletePost(postCreated.getPostId()));
    }


    @Test
    public void testUpdatePost() throws ParserConfigurationException, IOException, SAXException {
        userSession = new UserSession();
        userSession.login("user1", "qwerty");
        userSession.createPost("test post");
        Post postCreated = userSession.allPosts().get(userSession.allPosts().size()-1);
        Post otherPost = Post.readFromPost().get(3);

        // backup the users in user.xml
        List<User> users = User.readUsers();

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
        assertFalse(userSession.likePost("1")); // test like on post that doesn't exist
        assertFalse(userSession.unlikePost("1")); // test unlike on post that doesn't exist
        Post likedPost = Post.readFromPost().get(3);
        assertEquals(userSession.getUserName(), likedPost.getLikes().get(likedPost.getLikes().size()-1));
        assertTrue(userSession.unlikePost(otherPost.getPostId()));
        Post unlikedPost = Post.readFromPost().get(3);
        assertFalse(userSession.unlikePost(unlikedPost.getPostId())); // test unlike on unliked post

        // test comment post
        assertTrue(userSession.commentPost(postCreated.getPostId(), "what a good post!"));
        assertTrue(userSession.commentPost(postCreated.getPostId(), "what a good post, again!")); // test comment twice
        assertFalse(userSession.commentPost("1", "where is the post?")); // test comment on post that doesn't exist
        Post commetedPost = userSession.allPosts().get(userSession.allPosts().size()-1);
        assertEquals("what a good post!", commetedPost.getComments().get(0).getContent());
        assertEquals("what a good post, again!", commetedPost.getComments().get(1).getContent());
        assertEquals(userSession.getUserName(), commetedPost.getComments().get(0).getAuthor());

        // test follow and unfollow post
        assertTrue(userSession.followPost(otherPost.getPostId()));
        assertFalse(userSession.followPost(otherPost.getPostId())); // test follow followed post
        assertFalse(userSession.followPost("1")); // test follow post that doesn't exist
        assertFalse(userSession.unfollowPost("1")); // test unfollow post that doesn't exist
        Post followedPost = Post.readFromPost().get(3);
        assertEquals(userSession.getUserName(), followedPost.getObservers().get(followedPost.getObservers().size()-1));
        assertTrue(userSession.unfollowPost(otherPost.getPostId()));
        Post unfollowedPost = Post.readFromPost().get(3);
        assertFalse(userSession.unfollowPost(otherPost.getPostId())); // test unfollow unfollowed post

        // restore the original users into user.xml
        User.writeToUser(users);

        userSession.deletePost(postCreated.getPostId());
    }

    @Test
    public void testNotification() throws ParserConfigurationException, IOException, SAXException {
        userSession = new UserSession();
        userSession.register("user2", "qwerty", "user2@mail.com", "First2", "Last2", "123456789");


        // test comment notification
        userSession.createPost("test post");
        Post postCreated = userSession.allPosts().get(userSession.allPosts().size()-1);
        userSession.commentPost(postCreated.getPostId(), "commented");
        List<String> newNotifications = userSession.updateNotifications();
        assertEquals("Someone commented a post you followed:"+postCreated.getPostId(), newNotifications.get(0));

        // test like notification
        UserSession otherUserSession = new UserSession();
        otherUserSession.login("user3", "qwerty");
        otherUserSession.likePost(postCreated.getPostId());
        newNotifications = userSession.updateNotifications();
        assertEquals("Someone liked a post you followed:"+postCreated.getPostId(), newNotifications.get(1));

        // test post update notification
        userSession.editPost(postCreated.getPostId(), "edited");
        newNotifications = userSession.updateNotifications();
        assertEquals("Update on post you followed:"+postCreated.getPostId(), newNotifications.get(2));

        // test clear notification
        assertTrue(userSession.clearNotifications());

        userSession.deletePost(postCreated.getPostId());
        userSession.deleteAccount();
    }

}
