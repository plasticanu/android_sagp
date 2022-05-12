package com.example.comp6442_group_assignment;

import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

import android.util.Log;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserTest {

    User user1 = new User("user1", "qwerty");
    User invalidUser = new User("user", "qwerty1");

    @Test
    public void isValidTest() throws ParserConfigurationException, IOException, SAXException {
        assertTrue(user1.isValid());
        assertFalse(invalidUser.isValid());
    }
}
