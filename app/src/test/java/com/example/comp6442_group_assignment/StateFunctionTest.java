package com.example.comp6442_group_assignment;

import com.example.comp6442_group_assignment.State.GuestState;
import com.example.comp6442_group_assignment.State.LoggedInState;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

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


}
