package com.example.comp6442_group_assignment.FakeServerStuff;

import com.example.comp6442_group_assignment.Comment;
import com.example.comp6442_group_assignment.Post;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class CreatePostXml {
    public static void main(String[] args) throws ParserConfigurationException {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("comment1", "user1", "2020-01-01"));
        comments.add(new Comment("comment2", "user2", "2020-01-02"));
        Post post1 = new Post("00000001","This is a test post", "user1", 0, "2020-01-01", comments);
        Post post2 = new Post("00000002","This is a test post", "user1", 0, "2020-01-01", new ArrayList<Comment>());
        Post post3 = new Post("00000003","This is a test post", "user2", 0, "2020-01-01", new ArrayList<Comment>());
        Post post4 = new Post("00000004","This is a test post", "user3", 0, "2020-01-01", new ArrayList<Comment>());
        Post post5 = new Post("00000005","This is a test post", "user1", 0, "2020-01-01", new ArrayList<Comment>());
        Post post6 = new Post("00000006","This is a test post", "user1", 0, "2020-01-01", new ArrayList<Comment>());

        List<Post> posts = new ArrayList<>();
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        posts.add(post4);
        posts.add(post5);
        posts.add(post6);

        Post.writeToPost(posts);
    }

    // write doc to output stream
    public static void writeXml(Document doc,
                                OutputStream output)
            throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);

    }
}
