//package com.example.comp6442_group_assignment.FakeServerStuff;
//
//import android.annotation.SuppressLint;
//import android.os.Build;
//import androidx.annotation.RequiresApi;
//import com.example.comp6442_group_assignment.Comment;
//import com.example.comp6442_group_assignment.Post;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.*;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//
///**
// * This class was used for creating the XML file. It is officially abandoned.
// * The commented code is kept for future reference.
// * Nobody should use this class.
// */
//
////package com.example.comp6442_group_assignment.FakeServerStuff;
////
////import com.example.comp6442_group_assignment.Comment;
////import com.example.comp6442_group_assignment.Post;
////import org.w3c.dom.Document;
////
////import javax.xml.parsers.ParserConfigurationException;
////import javax.xml.transform.OutputKeys;
////import javax.xml.transform.Transformer;
////import javax.xml.transform.TransformerException;
////import javax.xml.transform.TransformerFactory;
////import javax.xml.transform.dom.DOMSource;
////import javax.xml.transform.stream.StreamResult;
////import java.io.OutputStream;
////import java.util.ArrayList;
////import java.util.List;
////
////public class CreatePostXml {
////    public static void main(String[] args) throws ParserConfigurationException {
////        List<Comment> comments = new ArrayList<>();
////        comments.add(new Comment("comment1", "user1", "2020-01-01"));
////        comments.add(new Comment("comment2", "user2", "2020-01-02"));
////        List<String> likes = new ArrayList<>();
////        likes.add("user1");
////        likes.add("user2");
////        Post post1 = new Post("00000001","This is a test post", "user1", new ArrayList<>(), "2020-01-01", comments);
////        Post post2 = new Post("00000002","This is a test post", "user1", new ArrayList<>(), "2020-01-01", new ArrayList<Comment>());
////        Post post3 = new Post("00000003","This is a test post", "user2", likes, "2020-01-01", new ArrayList<Comment>());
////        Post post4 = new Post("00000004","This is a test post", "user3", new ArrayList<>(), "2020-01-01", new ArrayList<Comment>());
////        Post post5 = new Post("00000005","This is a test post", "user1", new ArrayList<>(), "2020-01-01", new ArrayList<Comment>());
////        Post post6 = new Post("00000006","This is a test post", "user1", new ArrayList<>(), "2020-01-01", new ArrayList<Comment>());
////
////        List<Post> posts = new ArrayList<>();
////        posts.add(post1);
////        posts.add(post2);
////        posts.add(post3);
////        posts.add(post4);
////        posts.add(post5);
////        posts.add(post6);
////
////        Post.writeToPost(posts);
////    }
////
////    // write doc to output stream
////    public static void writeXml(Document doc,
////                                OutputStream output)
////            throws TransformerException {
////
////        TransformerFactory transformerFactory = TransformerFactory.newInstance();
////        Transformer transformer = transformerFactory.newTransformer();
////
////        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
////
////        DOMSource source = new DOMSource(doc);
////        StreamResult result = new StreamResult(output);
////
////        transformer.transform(source, result);
////
////    }
////}
//public class CreatePostXml {
//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
//        String macbeth = readFileAsString("app/src/main/assets/macbeth.txt");
//        String[] sections = macbeth.split("\n");
//        List<String> parts = Arrays.asList(sections);
//        List<String> contents = new ArrayList<>();
//
//        for (String part : parts) {
//            System.out.println(parts.indexOf(part) + part);
//        }
//
//        int counter = 0;
//        String content = "";
//        for (String part : parts) {
//            counter++;
//            content = content + part;
//            if (counter == 5) {
//                counter = 0;
//                contents.add(content);
//                content = "";
//            }
//        }
//
//        for (String content2 : contents) {
//            content2 = content2 + "\n"; // add new line at the end of each content
//        }
//
//        Post postToWrite;
//        postToWrite = new Post("11111111", "This is a test post", "user1", new ArrayList<>(), "2020-01-01", new ArrayList<Comment>(), new ArrayList<String>());
//        Post.addToPost(postToWrite);
//
//        counter = 0;
//        String username;
//        for (String content1 : contents) {
//            if (counter % 3 == 0) {
//                username = "user1";
//            } else if (counter % 3 == 1) {
//                username = "user3";
//            } else {
//                username = "user4";
//            }
//            @SuppressLint({"NewApi", "LocalSuppress"}) LocalDateTime now = LocalDateTime.now();
//            assert now != null;
//            String dateTime = now.toString();
//
//
//            postToWrite = new Post("11111111", content1, username, new ArrayList<>(), dateTime, new ArrayList<Comment>(), new ArrayList<String>());
//            Post.addToPost(postToWrite);
//            counter++;
//        }
//
//        List<Post> user1 = Post.getAllPosts("user1");
//        for (Post post : user1) {
//            System.out.println(post.getContent());
//        }
//
//
//    }
//
//    /**
//     * This perfect method is provided by a kind man b.roth from
//     * https://stackoverflow.com/questions/1656797/how-to-read-a-file-into-string-in-java
//     * @param filePath
//     * @return
//     * @throws IOException
//     */
//    private static String readFileAsString(String filePath) throws IOException {
//        StringBuffer fileData = new StringBuffer();
//        BufferedReader reader = new BufferedReader(
//                new FileReader(filePath));
//        char[] buf = new char[1024];
//        int numRead=0;
//        while((numRead=reader.read(buf)) != -1){
//            String readData = String.valueOf(buf, 0, numRead);
//            fileData.append(readData);
//        }
//        reader.close();
//        return fileData.toString();
//    }
//}