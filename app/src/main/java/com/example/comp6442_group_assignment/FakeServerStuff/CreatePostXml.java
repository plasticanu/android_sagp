package com.example.comp6442_group_assignment.FakeServerStuff;

import android.annotation.SuppressLint;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.example.comp6442_group_assignment.Comment;
import com.example.comp6442_group_assignment.Post;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class was used for creating the XML file. It is officially abandoned.
 * The commented code is kept for future reference.
 * Nobody should use this class.
 */
public class CreatePostXml {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String macbeth = readFileAsString("app/src/main/assets/macbeth.txt");
        String[] sections = macbeth.split("\n");
        List<String> parts = Arrays.asList(sections);
        List<String> contents = new ArrayList<>();

        for (String part : parts) {
            System.out.println(parts.indexOf(part) + part);
        }

        int counter = 0;
        String content = "";
        for (String part : parts) {
            counter++;
            content = content + part;
            if (counter == 1) {
                counter = 0;
                contents.add(content);
                content = "";
            }
        }

        for (String content2 : contents) {
            content2 = content2 + "\n"; // add new line at the end of each content
        }

        Post postToWrite;
        postToWrite = new Post("11111111", "This is a test post", "user1", new ArrayList<>(), "2020-01-01", new ArrayList<Comment>(), new ArrayList<String>());
        Post.addToPost(postToWrite);

        counter = 0;
        String username;
        for (String content1 : contents) {
            if (counter % 3 == 0) {
                username = "user1";
            } else if (counter % 3 == 1) {
                username = "user3";
            } else {
                username = "user4";
            }
            @SuppressLint({"NewApi", "LocalSuppress"}) LocalDateTime now = LocalDateTime.now();
            assert now != null;
            String dateTime = now.toString();


            postToWrite = new Post("11111111", content1, username, new ArrayList<>(), dateTime, new ArrayList<Comment>(), new ArrayList<String>());
            Post.addToPost(postToWrite);
            counter++;
        }

        List<Post> user1 = Post.getAllPosts("user1");
        for (Post post : user1) {
            System.out.println(post.getContent());
        }


    }

    /**
     * This perfect method is provided by a kind man b.roth from
     * https://stackoverflow.com/questions/1656797/how-to-read-a-file-into-string-in-java
     * @param filePath
     * @return
     * @throws IOException
     */
    private static String readFileAsString(String filePath) throws IOException {
        StringBuffer fileData = new StringBuffer();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead=0;
        while((numRead=reader.read(buf)) != -1){
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }
}