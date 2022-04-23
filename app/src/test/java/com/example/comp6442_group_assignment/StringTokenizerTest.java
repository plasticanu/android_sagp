package com.example.comp6442_group_assignment;

import com.example.comp6442_group_assignment.Search.StringTokenizer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StringTokenizerTest {
    @Test(timeout = 1000)
    public void testStringTokenizer() {
        String testString = "Today is a good day. I am happy.";
        List<String> expected = new ArrayList<String>();
        List<String> actual = new ArrayList<String>();
        expected.add("Today is a good day.");
        expected.add("I am happy.");
        StringTokenizer stringTokenizer = new StringTokenizer(testString);
        while (stringTokenizer.hasNext()) {
            String token = stringTokenizer.current();
            actual.add(token);
            stringTokenizer.next();
        }
        assertEquals(expected, actual);
    }
}
