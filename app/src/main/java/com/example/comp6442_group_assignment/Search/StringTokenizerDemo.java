package com.example.comp6442_group_assignment.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will tokenize the string into different sentences.
 */
public class StringTokenizerDemo {
    public static void main(String[] args) {
        String testString = "Today is a good day. I am happy.";
        StringTokenizer stringTokenizer = new StringTokenizer(testString);
        List<String> expected = new ArrayList<String>();
        List<String> actual = new ArrayList<String>();
        expected.add("Today is a good day");
        expected.add("I am happy");

        while (stringTokenizer.hasNext()) {
            String token = stringTokenizer.current();
            actual.add(token);
            stringTokenizer.next();
        }

        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);

        testString = "Today is a good day. ";
        SentenceTokenizer sentenceTokenizer = new SentenceTokenizer(testString);
        expected = new ArrayList<String>();
        actual = new ArrayList<String>();
        expected.add("Today");
        expected.add("is");
        expected.add("a");
        expected.add("good");
        expected.add("day");

        while (sentenceTokenizer.hasNext()) {
            String token = sentenceTokenizer.current();
            actual.add(token);
            sentenceTokenizer.next();
        }

        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);

        System.out.println("=======================================================");
        testString = "Today is a good day. I have learnt 1+1=2. #LearntSomethingNewToday @Bernardo \\Adam";
        stringTokenizer = new StringTokenizer(testString);
        String originalString = testString;
        List<String> firstStep = new ArrayList<String>();
        List<String> secondStep = new ArrayList<String>();

        while (stringTokenizer.hasNext()) {
            String token = stringTokenizer.current();
            firstStep.add(token);
            stringTokenizer.next();
        }
        for (String s : firstStep) {
            sentenceTokenizer = new SentenceTokenizer(s); // Create a new sentence tokenizer for each string
            while (sentenceTokenizer.hasNext()) {
                String token = sentenceTokenizer.current();
                secondStep.add(token);
                sentenceTokenizer.next();
            }
        }

        System.out.println("Original String: " + originalString);
        System.out.println("First Step: " + firstStep);
        System.out.println("Second Step: " + secondStep);
    }
}
