package com.example.comp6442_group_assignment;

import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserTest {

    User user1 = new User("user1", "qwerty");
    User invalidUser = new User("user", "qwerty1");
    User newUser = new User("user5", "1234", "addedUser@dmail.com", "John", "Adam", "123", new ArrayList<>(), true);

    @Test
    public void isValidTest() throws ParserConfigurationException, IOException, SAXException {
        assertTrue(user1.isValid());
        assertFalse(invalidUser.isValid());
    }

    @Test
    public void registerTest() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        assertTrue(User.register(newUser.getUserName(), newUser.getPassword(), newUser.getEmail(),
                newUser.getFirstName(), newUser.getLastName(), newUser.getPhoneNumber()));
        assertFalse(User.isRegistered("user5"));
    }
}
